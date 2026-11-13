package app.diktafon.recovery.domain

import kotlinx.coroutines.flow.Flow

interface ProcessingJobRepository {
    suspend fun markInProgress(recordingId: Long, type: ProcessingJobType, updatedAtMillis: Long): Long
    suspend fun markDone(jobId: Long, updatedAtMillis: Long)
    suspend fun markFailed(jobId: Long, updatedAtMillis: Long)

    // Zastizena v IN_PROGRESS pri nacteni = byla prerusena (padem/restartem) uprostred placeneho
    // volani - viz UC-008/NFR-REL-002/NFR-REL-003, ne beznej bezici stav v ramci teze relace.
    fun observeInterrupted(): Flow<List<ProcessingJob>>
}
