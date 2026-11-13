package app.diktafon.recovery

import app.diktafon.recovery.domain.ProcessingJob
import app.diktafon.recovery.domain.ProcessingJobRepository
import app.diktafon.recovery.domain.ProcessingJobStatus
import app.diktafon.recovery.domain.ProcessingJobType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeProcessingJobRepository : ProcessingJobRepository {
    val jobs = mutableListOf<ProcessingJob>()
    private val interruptedFlow = MutableStateFlow<List<ProcessingJob>>(emptyList())

    override suspend fun markInProgress(recordingId: Long, type: ProcessingJobType, updatedAtMillis: Long): Long {
        val id = jobs.size.toLong() + 1
        jobs += ProcessingJob(id, recordingId, type, ProcessingJobStatus.IN_PROGRESS, updatedAtMillis)
        refreshInterrupted()
        return id
    }

    override suspend fun markDone(jobId: Long, updatedAtMillis: Long) {
        updateStatus(jobId, ProcessingJobStatus.DONE, updatedAtMillis)
    }

    override suspend fun markFailed(jobId: Long, updatedAtMillis: Long) {
        updateStatus(jobId, ProcessingJobStatus.FAILED, updatedAtMillis)
    }

    private fun updateStatus(jobId: Long, status: ProcessingJobStatus, updatedAtMillis: Long) {
        val index = jobs.indexOfFirst { it.id == jobId }
        if (index == -1) return
        jobs[index] = jobs[index].copy(status = status, updatedAtMillis = updatedAtMillis)
        refreshInterrupted()
    }

    private fun refreshInterrupted() {
        interruptedFlow.value = jobs.filter { it.status == ProcessingJobStatus.IN_PROGRESS }
    }

    override fun observeInterrupted(): Flow<List<ProcessingJob>> = interruptedFlow
}
