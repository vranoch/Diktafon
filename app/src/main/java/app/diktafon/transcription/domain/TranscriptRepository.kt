package app.diktafon.transcription.domain

import kotlinx.coroutines.flow.Flow

interface TranscriptRepository {
    suspend fun saveNewRevision(
        recordingId: Long,
        text: String,
        languageCode: String,
        providerName: String,
        modelName: String,
        createdAtMillis: Long,
    ): Transcript

    fun observeLatest(recordingId: Long): Flow<Transcript?>
    fun observeAllRevisions(recordingId: Long): Flow<List<Transcript>>
}
