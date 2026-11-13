package app.diktafon.transcription

import app.diktafon.transcription.domain.SpeechToTextProvider
import app.diktafon.transcription.domain.Transcript
import app.diktafon.transcription.domain.TranscriptRepository
import app.diktafon.transcription.domain.TranscriptionRequest
import app.diktafon.transcription.domain.TranscriptionResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeSpeechToTextProvider(private val result: TranscriptionResult) : SpeechToTextProvider {
    var lastRequest: TranscriptionRequest? = null

    override suspend fun transcribe(request: TranscriptionRequest): TranscriptionResult {
        lastRequest = request
        return result
    }
}

class FakeTranscriptRepository : TranscriptRepository {
    val saved = mutableListOf<Transcript>()
    private val latestFlow = MutableStateFlow<Transcript?>(null)
    private val allFlow = MutableStateFlow<List<Transcript>>(emptyList())

    override suspend fun saveNewRevision(
        recordingId: Long,
        text: String,
        languageCode: String,
        providerName: String,
        modelName: String,
        createdAtMillis: Long,
    ): Transcript {
        val nextVersion = (saved.filter { it.recordingId == recordingId }.maxOfOrNull { it.version } ?: 0) + 1
        val transcript = Transcript(
            id = saved.size.toLong() + 1,
            recordingId = recordingId,
            text = text,
            languageCode = languageCode,
            providerName = providerName,
            modelName = modelName,
            version = nextVersion,
            createdAtMillis = createdAtMillis,
        )
        saved += transcript
        latestFlow.value = transcript
        allFlow.value = saved.toList()
        return transcript
    }

    override fun observeLatest(recordingId: Long): Flow<Transcript?> = latestFlow
    override fun observeAllRevisions(recordingId: Long): Flow<List<Transcript>> = allFlow
}
