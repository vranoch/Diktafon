package app.diktafon.recording.data

import app.diktafon.recording.domain.Recording
import app.diktafon.recording.domain.RecordingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RecordingRepositoryImpl @Inject constructor(
    private val dao: RecordingDao,
) : RecordingRepository {

    override suspend fun saveRecording(recording: Recording): Long =
        dao.insert(recording.toEntity())

    override fun observeAll(): Flow<List<Recording>> =
        dao.observeAll().map { entities -> entities.map { it.toDomain() } }
}

private fun Recording.toEntity() = RecordingEntity(
    id = id,
    name = name,
    note = note,
    folderId = folderId,
    filePath = filePath,
    durationMs = durationMs,
    createdAtMillis = createdAtMillis,
)

private fun RecordingEntity.toDomain() = Recording(
    id = id,
    name = name,
    note = note,
    folderId = folderId,
    filePath = filePath,
    durationMs = durationMs,
    createdAtMillis = createdAtMillis,
)
