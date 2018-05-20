package polybius.android.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import polybius.android.repo.InMemoryStateRepository
import polybius.common.models.Task

class PlaybackControlViewModel(stateRepository: InMemoryStateRepository): ViewModel() {
    val isConnected : LiveData<Boolean> = stateRepository.isConnected
    val tasks : LiveData<Map<String, Task>> = stateRepository.tasks
}