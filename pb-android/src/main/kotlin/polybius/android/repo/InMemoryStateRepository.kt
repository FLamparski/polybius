package polybius.android.repo

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import polybius.android.kextensions.tag
import polybius.android.model.TaskLoaderState
import polybius.common.models.MessageType
import polybius.common.models.PolybiusMessage
import polybius.common.models.Task
import java.util.*

class InMemoryStateRepository {
    val isConnected = MutableLiveData<Boolean>()
    val taskLoadingState = MutableLiveData<TaskLoaderState>()
    val tasks = MutableLiveData<Map<String, Task>>()

    init {
        isConnected.value = false
        tasks.value = LinkedHashMap()
        taskLoadingState.value = TaskLoaderState.LOADING
    }

    fun handleMessage(message: PolybiusMessage) {
        Log.i(tag(this), "handleMessage ${message.messageType}")
        Log.d(tag(this), message.toString())
        when (message.messageType) {
            MessageType.SERVER_TASK_ADDED,
            MessageType.EXECUTOR_TASK_ACCEPTED,
            MessageType.EXECUTOR_TASK_PLAYING,
            MessageType.EXECUTOR_TASK_PAUSED,
            MessageType.EXECUTOR_TASK_ERROR -> updateTask(message.task)
            MessageType.SERVER_TASK_REMOVED -> removeTask(message.task)
            else -> {}
        }
    }

    private fun updateTask(task: Task?) {
        if (task?.id == null) {
            return
        }

        val nextTasks = tasks.value!! + (task.id!! to task)
        tasks.postValue(nextTasks)
    }

    private fun removeTask(task: Task?) {
        if (task?.id == null) {
            return
        }

        val nextTasks = tasks.value!! - task.id!!
        tasks.postValue(nextTasks)
    }
}