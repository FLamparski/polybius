package polybius.android.fake

import polybius.android.kextensions.capitalizeWords
import polybius.android.repo.InMemoryStateRepository
import polybius.common.models.*
import polybius.common.platform.DateTime
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.min

class FakeDataGenerator(private val stateRepository: InMemoryStateRepository) {
    private val random = Random()

    private val user = User("username", "deviceName")
    private val tasks: Deque<Task> = ArrayDeque(15)

    private var previousMessage: PolybiusMessage? = null

    fun tick(tickInterval: Long) {
        if (previousMessage == null) {
            val task = Task(id(), 0, title(), user, DateTime.now(), TaskType.YOUTUBE, url(), TaskState.default())
            tasks.offer(task)
            stateRepository.handleMessage(PolybiusMessage.taskAdded(task))
            val msg = PolybiusMessage.taskAccepted(task.accepted(TimeUnit.MINUTES.toSeconds(15)))
            stateRepository.handleMessage(msg)
            previousMessage = msg
            return
        }

        val pMsg = previousMessage!!
        when (pMsg.messageType) {
            MessageType.EXECUTOR_TASK_ACCEPTED,
            MessageType.EXECUTOR_TASK_PLAYING -> {
                val task = pMsg.task!!
                val duration = task.state!!.duration!!
                val newPos = if (task.state!!.position != null) min(task.state!!.position!! + TimeUnit.MILLISECONDS.toSeconds(tickInterval), duration) else 0
                if (newPos <= duration) {
                    val playingTask = task.playing(duration, newPos)
                    val msg = PolybiusMessage.taskPlaying(playingTask)
                    stateRepository.handleMessage(msg)
                    previousMessage = msg
                }
                else {
                    val msg = PolybiusMessage.taskRemoved(task)
                    stateRepository.handleMessage(msg)
                    previousMessage = msg
                }
            }
            MessageType.SERVER_TASK_REMOVED -> {
                if (tasks.size < 2) {
                    stateRepository.handleMessage(PolybiusMessage.executorIdle("fake-executor"))
                }
                else {
                    tasks.removeFirst()
                    val task = tasks.peekFirst()
                    stateRepository.handleMessage(PolybiusMessage.taskAdded(task))
                    val msg = PolybiusMessage.taskAccepted(task.accepted(TimeUnit.MINUTES.toSeconds(15)))
                    stateRepository.handleMessage(msg)
                    previousMessage = msg
                }
            }
            else -> {}
        }

        if (random.nextFloat() <= 0.01 && tasks.size < 15) {
            val order = tasks.peekLast().order + 1
            val task = Task(id(), order, title(), user, DateTime.now(), TaskType.YOUTUBE, url(), TaskState.default())
            tasks.offer(task)
            stateRepository.handleMessage(PolybiusMessage.taskAdded(task))
        }
    }

    private fun id() = UUID.randomUUID().toString()

    private fun url() = "fake://youtube.com/watch?v=${UUID.randomUUID().toString().split("-")[0]}"

    private fun title() = capitalizeWords(generateProjectName(random)).joinToString(" ")
}