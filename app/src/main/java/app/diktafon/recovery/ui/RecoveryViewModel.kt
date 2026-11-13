package app.diktafon.recovery.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.diktafon.recording.domain.Clock
import app.diktafon.recovery.domain.ProcessingJobRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecoveryViewModel @Inject constructor(
    private val processingJobRepository: ProcessingJobRepository,
    private val clock: Clock,
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecoveryUiState())
    val uiState: StateFlow<RecoveryUiState> = _uiState.asStateFlow()

    fun checkForInterruptedJobs() {
        viewModelScope.launch {
            processingJobRepository.observeInterrupted().collect { jobs ->
                _uiState.update { it.copy(interruptedJobs = jobs) }
            }
        }
    }

    fun dismissJob(jobId: Long) {
        viewModelScope.launch {
            processingJobRepository.markFailed(jobId, clock.nowMillis())
        }
    }
}
