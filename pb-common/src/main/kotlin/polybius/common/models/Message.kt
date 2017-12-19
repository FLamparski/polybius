package polybius.common.models

import polybius.common.platform.DateTime

data class Message (
        val messageType: MessageType,
        val timestamp: Long,
        val task: Task?,
        val error: String?,
        val name: String?
) {
    companion object {
        fun taskAdded(task: Task) = Message(
                messageType = MessageType.SERVER_TASK_ADDED,
                timestamp = DateTime.now().toUnix(),
                task = task,
                error = null,
                name = "${task.url} submitted by ${task.submitter.username}"
        )

        fun executorConnected(name: String) = Message(
                messageType = MessageType.EXECUTOR_CONNECTED,
                timestamp = DateTime.now().toUnix(),
                task = null,
                error = null,
                name = name
        )

        fun error(errorMessage: String) = Message(
                messageType = MessageType.ERROR,
                timestamp = DateTime.now().toUnix(),
                task = null,
                name = null,
                error = errorMessage
        )
    }
}