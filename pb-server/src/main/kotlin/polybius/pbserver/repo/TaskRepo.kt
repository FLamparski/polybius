package polybius.pbserver.repo

import org.springframework.stereotype.Repository
import polybius.common.models.Task
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.stream.Stream

@Repository
class TaskRepo {
    private val tasks = ConcurrentHashMap<String, Task>()

    fun findById(id: String) = if (tasks.containsKey(id)) tasks[id] else null

    fun getAllOrderByOrder(): Stream<Task> = tasks.values
            .stream()
            .sorted({ a, b -> (a.order - b.order).toInt() })

    fun getNext(): Optional<Task> = getAllOrderByOrder().findFirst()

    fun save(task: Task): Task {
        tasks.put(task.id!!, task)
        return task
    }

    fun delete(id: String) = tasks.remove(id)
}