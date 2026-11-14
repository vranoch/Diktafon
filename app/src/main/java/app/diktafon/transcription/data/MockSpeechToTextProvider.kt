package app.diktafon.transcription.data

import app.diktafon.transcription.domain.SpeechToTextProvider
import app.diktafon.transcription.domain.TranscriptionRequest
import app.diktafon.transcription.domain.TranscriptionResult
import javax.inject.Inject

// Vychozi simulace (ADR-004) - pouziva se, dokud je v Administraci vybrana "Simulace", aby
// aplikaci slo zkouset bez nakladu a bez API klice. Realna implementace: OpenAiSpeechToTextProvider.
class MockSpeechToTextProvider @Inject constructor() : SpeechToTextProvider {

    override suspend fun transcribe(request: TranscriptionRequest): TranscriptionResult {
        if (request.audioFile.length() == 0L) {
            return TranscriptionResult.Failure.EmptyResult
        }
        return TranscriptionResult.Success(
            text = "⚠ SIMULACE PŘEPISU — v Administraci je vybraná Simulace, ne reálná STT " +
                "služba, toto NENÍ skutečný přepis nahrávky.\n" +
                "Soubor: ${request.audioFile.name}\n" +
                "Jazyk: ${request.languageCode}",
            providerName = "mock",
            modelName = "mock-v1",
        )
    }
}
