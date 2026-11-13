package app.diktafon.transcription.data

import app.diktafon.transcription.domain.Transcript
import app.diktafon.transcription.domain.TranscriptRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TranscriptRepositoryImpl @Inject constructor(
    private val dao: TranscriptDao,
) : TranscriptRepository {

    override suspend fun saveNewRevision(
        recordingId: Long,
        text: String,
        languageCode: String,
        providerName: String,
        modelName: String,
        createdAtMillis: Long,
    ): Transcript {
        val nextVersion = (dao.maxVersion(recordingId) ?: 0) + 1
        val entity = TranscriptEntity(
            recordingId = recordingId,
            text = text,
            languageCode = languageCode,
            providerName = providerName,
            modelName = modelName,
            version = nextVersion,
            createdAtMillis = createdAtMillis,
        )
        val id = dao.insert(entity)
        return entity.copy(id = id).toDomain()
    }

    override fun observeLatest(recordingId: Long): Flow<Transcript?> =
        dao.observeLatest(recordingId).map { it?.toDomain() }

    override fun observeAllRevisions(recordingId: Long): Flow<List<Transcript>> =
        dao.observeAllRevisions(recordingId).map { entities -> entities.map { it.toDomain() } }
}

private fun TranscriptEntity.toDomain() = Transcript(
    id = id,
    recordingId = recordingId,
    text = text,
    languageCode = languageCode,
    providerName = providerName,
    modelName = modelName,
    version = version,
    createdAtMillis = createdAtMillis,
)
