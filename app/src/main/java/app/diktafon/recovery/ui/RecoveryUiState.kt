package app.diktafon.recovery.ui

import app.diktafon.recovery.domain.ProcessingJob

data class RecoveryUiState(
    val interruptedJobs: List<ProcessingJob> = emptyList(),
)
