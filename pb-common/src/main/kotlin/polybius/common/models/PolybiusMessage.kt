package polybius.common.models

import polybius.common.platform.DateTime

data class PolybiusMessage(
        val messageType: MessageType,
        val timestamp: Long,
        val task: Task?,
        val error: String?,
        val name: String?
) {
    companion object {
        fun taskAdded(task: Task) = PolybiusMessage(
                messageType = MessageType.SERVER_TASK_ADDED,
                timestamp = DateTime.now().toUnix(),
                task = task,
                error = null,
                name = task.submittedString()
        )

        fun error(errorMessage: String) = PolybiusMessage(
                messageType = MessageType.ERROR,
                timestamp = DateTime.now().toUnix(),
                task = null,
                name = null,
                error = errorMessage
        )

        fun taskRemoved(deleted: Task) = PolybiusMessage(
                messageType = MessageType.SERVER_TASK_REMOVED,
                timestamp = DateTime.now().toUnix(),
                task = deleted,
                error = null,
                name = deleted.submittedString()
        )

        fun taskAccepted(acceptedTask: Task) = PolybiusMessage(
                messageType = MessageType.EXECUTOR_TASK_ACCEPTED,
                timestamp = DateTime.now().toUnix(),
                task = acceptedTask,
                error = null,
                name = acceptedTask.submittedString()
        )

        fun taskPlaying(playingTask: Task) = PolybiusMessage(
                messageType = MessageType.EXECUTOR_TASK_PLAYING,
                timestamp = DateTime.now().toUnix(),
                task = playingTask,
                error = null,
                name = playingTask.submittedString()
        )

        fun taskPaused(pausedTask: Task) = PolybiusMessage(
                messageType = MessageType.EXECUTOR_TASK_PAUSED,
                timestamp = DateTime.now().toUnix(),
                task = pausedTask,
                error = null,
                name = pausedTask.submittedString()
        )

        fun executorIdle(executorName: String) = PolybiusMessage(
                messageType = MessageType.EXECUTOR_IDLE,
                timestamp = DateTime.now().toUnix(),
                task = null,
                error = null,
                name = executorName
        )
    }
}