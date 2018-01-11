package polybius.pbserver.messaging

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component
import polybius.common.models.MessageType
import polybius.common.models.PolybiusMessage
import polybius.pbserver.service.TaskService

@Component
class TaskMessageHandler(
        val tasks: TaskService,
        val msgTemplate: SimpMessagingTemplate
) {
    @MessageMapping("/tasks")
    fun handleTaskMessage(@Payload message: PolybiusMessage) {
        when (message.messageType) {
            MessageType.EXECUTOR_TASK_ACCEPTED -> tasks.taskAccepted(message.task)
            MessageType.EXECUTOR_TASK_PLAYING -> tasks.taskPlaying(message.task)
//            MessageType.EXECUTOR_TASK_PAUSED -> tasks.taskPaused(message.task)
//            MessageType.EXECUTOR_TASK_ERROR -> tasks.taskError(message.task, message.error)
            else -> {}
        }
    }
}