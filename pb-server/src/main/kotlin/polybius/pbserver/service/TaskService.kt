package polybius.pbserver.service

import org.slf4j.LoggerFactory
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import polybius.common.models.PolybiusMessage
import polybius.common.models.Task
import polybius.common.models.TaskState
import polybius.common.platform.DateTime
import polybius.pbserver.repo.TaskRepo
import java.util.*

@Service
class TaskService(
        val taskRepo: TaskRepo,
        val msgTemplate: SimpMessagingTemplate)
{
    private val TOPIC = "/topic/tasks"

    private val log = LoggerFactory.getLogger(javaClass)

    fun findById(id: String) = taskRepo.findById(id)

    fun getAllOrderByOrder() = taskRepo.getAllOrderByOrder()

    fun getNext() = taskRepo.getNext()

    fun save(userTask: Task): Task {
        log.info("Saving task: {}", userTask)
        val id = UUID.randomUUID().toString()
        val task = userTask.copy(id = id, submittedOn = DateTime.now(), state = TaskState.default())
        taskRepo.save(task)
        log.info("Task saved: {}", task)
        msgTemplate.convertAndSend(TOPIC, PolybiusMessage.taskAdded(task))
        return task
    }

    fun delete(id: String): Task? {
        val task = taskRepo.findById(id)
        log.info("Deleting task: id={}, task={}", id, task)
        if (task != null && task.canRemove()) {
            taskRepo.delete(task.id!!)
            msgTemplate.convertAndSend(TOPIC, PolybiusMessage.taskRemoved(task))
            return task
        }
        return null
    }

    fun taskAccepted(userTask: Task?) = withTask(userTask?.id) {
        val acceptedTask = it.accepted()
        taskRepo.save(acceptedTask)
        msgTemplate.convertAndSend(TOPIC, PolybiusMessage.taskAccepted(acceptedTask))
    }

    fun taskPlaying(playingTask: Task?) = withTask(playingTask?.id) {
        taskRepo.save(playingTask!!)
        msgTemplate.convertAndSend(TOPIC, PolybiusMessage.taskPlaying(playingTask))
    }

    fun withTask(id: String?, closure: (Task) -> Unit) {
        if (id == null) return

        val task = taskRepo.findById(id)
        if (task != null) closure(task)
    }
}