package polybius.pbserver.service

import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.platform.commons.logging.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.test.context.junit.jupiter.SpringExtension
import polybius.common.models.*
import polybius.common.platform.DateTime
import polybius.pbserver.repo.TaskRepo
import java.util.concurrent.TimeUnit

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension::class)
class TaskServiceTests(
        @Autowired val taskRepo: TaskRepo
) {
    @Test
    fun `Emits a websocket message when a task is saved`() {
        val msgTemplate = mockk<SimpMessagingTemplate>(relaxed = true)
        val service = TaskService(taskRepo, msgTemplate)
        service.save(createTestTask())
        verify {
            msgTemplate.convertAndSend("/topic/tasks", assert<PolybiusMessage> {
                LoggerFactory.getLogger(javaClass).info { "Got message: " + it }
                it.messageType == MessageType.SERVER_TASK_ADDED
                        && it.task != null
                        && it.task!!.id != null
            })
        }
    }

    @Test
    fun `Emits a websocket message when a task is accepted`() {
        val task = taskRepo.save(createTestTask().copy(
                id = "testId",
                submittedOn = DateTime.now(),
                submitter = User("test", "test")))
        val msgTemplate = mockk<SimpMessagingTemplate>(relaxed = true)
        val service = TaskService(taskRepo, msgTemplate)
        service.taskAccepted(task)
        verify {
            msgTemplate.convertAndSend("/topic/tasks", assert<PolybiusMessage> {
                LoggerFactory.getLogger(javaClass).info { "Got message: " + it }
                it.messageType == MessageType.EXECUTOR_TASK_ACCEPTED
                        && it.task != null
                        && it.task!!.id == task.id
            })
        }
    }

    @Test
    fun `Emits a websocket message when a task is deleted`() {
        val task = taskRepo.save(createTestTask().copy(
                id = "testId",
                submittedOn = DateTime.now(),
                submitter = User("test", "test"),
                state = TaskState.default()))
        val msgTemplate = mockk<SimpMessagingTemplate>(relaxed = true)
        val service = TaskService(taskRepo, msgTemplate)
        service.delete(task.id!!)
        assert(service.findById(task.id!!) == null)
        verify {
            msgTemplate.convertAndSend("/topic/tasks", assert<PolybiusMessage> {
                LoggerFactory.getLogger(javaClass).info { "Got message: " + it }
                it.messageType == MessageType.SERVER_TASK_REMOVED
                        && it.task != null
                        && it.task!!.id == task.id
            })
        }
    }

    @Test
    fun `Emits a websocket message when a task play state is updated`() {
        val task = taskRepo.save(createTestTask().copy(
                id = "testId",
                submittedOn = DateTime.now(),
                submitter = User("test", "test"),
                state = TaskState(
                        scheduleState = ScheduleState.PLAYING,
                        playbackState = PlaybackState.NOT_PLAYING,
                        duration = TimeUnit.MINUTES.toMillis(42),
                        position = TimeUnit.MINUTES.toMillis(0)
                )
        ))
        taskRepo.save(task)
        val msgTemplate = mockk<SimpMessagingTemplate>(relaxed = true)
        val service = TaskService(taskRepo, msgTemplate)
        val playingTask = task.copy(
                state = task.state?.copy(
                        playbackState = PlaybackState.PLAYING,
                        position = TimeUnit.MINUTES.toMillis(10)))
        service.taskPlaying(playingTask)
        verify {
            msgTemplate.convertAndSend("/topic/tasks", assert<PolybiusMessage> {
                LoggerFactory.getLogger(javaClass).info { "Got message: " + it }
                it.messageType == MessageType.EXECUTOR_TASK_PLAYING
                        && it.task == playingTask
            })
        }
    }

    fun createTestTask() = Task(
            id = null,
            order = 1,
            type = TaskType.YOUTUBE,
            url = "https://youtube.com/watch?v=dQw4w9WgXcQ",
            submitter = User(
                    username = "test",
                    deviceName = "test"
            ),
            submittedOn = null,
            state = null
    )
}