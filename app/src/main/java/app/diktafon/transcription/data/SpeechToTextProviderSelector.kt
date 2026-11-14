package app.diktafon.transcription.data

import app.diktafon.settings.domain.ProviderConfigRepository
import app.diktafon.settings.domain.ProviderNames
import app.diktafon.transcription.domain.SpeechToTextProvider
import app.diktafon.transcription.domain.TranscriptionRequest
import app.diktafon.transcription.domain.TranscriptionResult
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/** Vybira mezi simulaci a realnym volanim podle Administraci nastaveneho providera (ADR-004). */
class SpeechToTextProviderSelector @Inject constructor(
    private val providerConfigRepository: ProviderConfigRepository,
    private val mock: MockSpeechToTextProvider,
    private val openAi: OpenAiSpeechToTextProvider,
) : SpeechToTextProvider {

    override suspend fun transcribe(request: TranscriptionRequest): TranscriptionResult {
        val providerName = providerConfigRepository.observe().first().sttProviderName
        return if (providerName.equals(ProviderNames.OPENAI, ignoreCase = true)) {
            openAi.transcribe(request)
        } else {
            mock.transcribe(request)
        }
    }
}
