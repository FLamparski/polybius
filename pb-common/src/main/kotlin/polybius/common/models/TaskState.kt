package polybius.common.models

data class TaskState (
        val scheduleState: ScheduleState,

        val playbackState: PlaybackState,
        val duration: Long?,
        val position: Long?
) {
    companion object {
        fun default() = TaskState(
                scheduleState = ScheduleState.SCHEDULED,
                playbackState = PlaybackState.NOT_PLAYING,
                duration = null,
                position = null
        )
    }
}