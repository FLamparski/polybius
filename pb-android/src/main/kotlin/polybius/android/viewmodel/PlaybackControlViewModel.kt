package polybius.android.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.AsyncTask
import polybius.common.models.Task
import polybius.common.models.TaskState
import polybius.common.models.TaskType
import polybius.common.models.User
import polybius.common.platform.DateTime

class PlaybackControlViewModel(): ViewModel() {
    private val u1 = User("user1", "device1")

    val tasks = MutableLiveData<List<Task>>()

    init {
        AsyncTask.execute {
            Thread.sleep(1000)
            tasks.postValue(listOf(
                    Task("task1", 0, u1, DateTime.now(), TaskType.YOUTUBE, ".", TaskState.default()),
                    Task("task2", 1, u1, DateTime.now(), TaskType.YOUTUBE, ".", TaskState.default())
            ))
        }
    }
}