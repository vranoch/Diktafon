package app.diktafon.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.diktafon.folders.domain.LastUsedFolderRepository
import app.diktafon.recovery.domain.ProcessingJobRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    lastUsedFolderRepository: LastUsedFolderRepository,
    processingJobRepository: ProcessingJobRepository,
) : ViewModel() {

    val lastUsedFolderId: StateFlow<Long?> = lastUsedFolderRepository.observe()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val hasInterruptedJobs: StateFlow<Boolean> = processingJobRepository.observeInterrupted()
        .map { it.isNotEmpty() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)
}
