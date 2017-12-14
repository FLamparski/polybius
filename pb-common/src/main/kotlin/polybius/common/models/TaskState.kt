package polybius.common.models

data class TaskState (
        val scheduleState: ScheduleState,

        val playbackState: PlaybackState,
        val duration: Long?,
        val position: Long?
)