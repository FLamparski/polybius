package polybius.pbserver.service

import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import polybius.common.models.Message
import polybius.common.models.Task
import polybius.pbserver.repo.TaskRepo

@Service
class TaskService(
        val taskRepo: TaskRepo,
        val msgTemplate: SimpMessagingTemplate)
{
    fun findById(id: String) = taskRepo.findById(id)

    fun getAllOrderByOrder() = taskRepo.getAllOrderByOrder()

    fun getNext() = taskRepo.getNext()

    fun save(userTask: Task): Task {
        val task = taskRepo.save(userTask)
        msgTemplate.convertAndSend("/topic/tasks", Message.taskAdded(task))
        return task
    }
}