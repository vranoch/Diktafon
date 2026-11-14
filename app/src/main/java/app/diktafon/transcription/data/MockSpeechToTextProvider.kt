package app.diktafon.transcription.data

import app.diktafon.transcription.domain.SpeechToTextProvider
import app.diktafon.transcription.domain.TranscriptionRequest
import app.diktafon.transcription.domain.TranscriptionResult
import javax.inject.Inject

// Zatím není vybrán žádný konkrétní STT provider (viz ADR-004) — toto je jediná dostupná
// implementace, dokud nevznikne rozhodnutí o konkrétní externí službě.
class MockSpeechToTextProvider @Inject constructor() : SpeechToTextProvider {

    override suspend fun transcribe(request: TranscriptionRequest): TranscriptionResult {
        if (request.audioFile.length() == 0L) {
            return TranscriptionResult.Failure.EmptyResult
        }
        return TranscriptionResult.Success(
            text = "⚠ SIMULACE PŘEPISU — žádný STT provider zatím není připojen (viz ADR-004), " +
                "toto NENÍ skutečný přepis nahrávky.\n" +
                "Soubor: ${request.audioFile.name}\n" +
                "Jazyk: ${request.languageCode}",
            providerName = "mock",
            modelName = "mock-v1",
        )
    }
}
