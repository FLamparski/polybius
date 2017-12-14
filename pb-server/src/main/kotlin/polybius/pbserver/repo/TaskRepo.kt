package polybius.pbserver.repo

import org.springframework.stereotype.Repository
import polybius.common.models.PlaybackState
import polybius.common.models.ScheduleState
import polybius.common.models.Task
import polybius.common.models.TaskState
import polybius.common.platform.DateTime
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.stream.Stream

@Repository
class TaskRepo {
    private val tasks = ConcurrentHashMap<String, Task>()

    fun findById(id: String) = if (id in tasks) tasks[id] else null

    fun getAllOrderByOrder(): Stream<Task> = tasks.values
            .stream()
            .sorted({ a, b -> (a.order - b.order).toInt() })

    fun getNext(): Optional<Task> = getAllOrderByOrder().findFirst()

    fun save(userTask: Task): Task {
        val id = UUID.randomUUID().toString()
        val task = userTask.copy(id = id, submittedOn = DateTime.now(), state = defaultState())
        tasks.put(id, task)
        return task
    }

    private fun defaultState() = TaskState(
            scheduleState = ScheduleState.SCHEDULED,
            playbackState = PlaybackState.NOT_PLAYING,
            duration = null,
            position = null
    )
}