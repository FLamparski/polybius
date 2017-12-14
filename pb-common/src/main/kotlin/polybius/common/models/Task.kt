package polybius.common.models
import polybius.common.platform.DateTime

data class Task (
        val id: String?,
        val order: Long,
        val submitter: User,
        val submittedOn: DateTime?,
        val type: TaskType,
        val url: String,
        val state: TaskState?
)