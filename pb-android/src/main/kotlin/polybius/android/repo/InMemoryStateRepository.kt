package polybius.android.repo

import android.arch.lifecycle.MutableLiveData

class InMemoryStateRepository {
    val isConnected = MutableLiveData<Boolean>()

    init {
        isConnected.value = false
    }
}