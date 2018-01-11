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
) {
    fun submittedString() = "$url submitted by ${submitter.username}"

    fun canRemove() = state?.scheduleState == ScheduleState.SCHEDULED
            || state?.scheduleState == ScheduleState.ERROR

    fun accepted(duration: Long? = null) = this.copy(
            state = state?.copy(
                    scheduleState = polybius.common.models.ScheduleState.PLAYING,
                    playbackState = polybius.common.models.PlaybackState.NOT_PLAYING,
                    duration = duration))

    fun playing(duration: Long? = null, position: Long? = null) = this.copy(
            state = state?.copy(
                    playbackState = polybius.common.models.PlaybackState.PLAYING,
                    duration = duration,
                    position = position))
}