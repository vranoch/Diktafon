package app.diktafon.recovery.data

import app.diktafon.recovery.domain.ProcessingJob
import app.diktafon.recovery.domain.ProcessingJobRepository
import app.diktafon.recovery.domain.ProcessingJobStatus
import app.diktafon.recovery.domain.ProcessingJobType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProcessingJobRepositoryImpl @Inject constructor(
    private val dao: ProcessingJobDao,
) : ProcessingJobRepository {

    override suspend fun markInProgress(recordingId: Long, type: ProcessingJobType, updatedAtMillis: Long): Long =
        dao.insert(
            ProcessingJobEntity(
                recordingId = recordingId,
                type = type.name,
                status = ProcessingJobStatus.IN_PROGRESS.name,
                updatedAtMillis = updatedAtMillis,
            ),
        )

    override suspend fun markDone(jobId: Long, updatedAtMillis: Long) =
        dao.updateStatus(jobId, ProcessingJobStatus.DONE.name, updatedAtMillis)

    override suspend fun markFailed(jobId: Long, updatedAtMillis: Long) =
        dao.updateStatus(jobId, ProcessingJobStatus.FAILED.name, updatedAtMillis)

    override fun observeInterrupted(): Flow<List<ProcessingJob>> =
        dao.observeByStatus(ProcessingJobStatus.IN_PROGRESS.name).map { entities -> entities.map { it.toDomain() } }
}

private fun ProcessingJobEntity.toDomain() = ProcessingJob(
    id = id,
    recordingId = recordingId,
    type = ProcessingJobType.valueOf(type),
    status = ProcessingJobStatus.valueOf(status),
    updatedAtMillis = updatedAtMillis,
)
