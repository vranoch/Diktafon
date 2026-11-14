package app.diktafon.transcription.data

import app.diktafon.settings.FakeApiKeyRepository
import app.diktafon.settings.FakeProviderConfigRepository
import app.diktafon.settings.domain.ProviderConfig
import app.diktafon.settings.domain.ProviderNames
import app.diktafon.transcription.domain.TranscriptionRequest
import app.diktafon.transcription.domain.TranscriptionResult
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File

class SpeechToTextProviderSelectorTest {

    // Soubor neexistuje -> MockSpeechToTextProvider vraci deterministicky EmptyResult.
    private val audioFile = File("/tmp/neexistujici-nahravka.m4a")

    // Bez ulozeneho klice vraci OpenAiSpeechToTextProvider deterministicky Unauthorized,
    // aniz by se pokusil o realne sitove volani - staci k odliseni od mocku v testu.
    private val openAi = OpenAiSpeechToTextProvider(
        apiKeyRepository = FakeApiKeyRepository(),
        providerConfigRepository = FakeProviderConfigRepository(),
        httpClient = OkHttpClient(),
    )

    private suspend fun selector(providerName: String): SpeechToTextProviderSelector {
        val providerConfigRepository = FakeProviderConfigRepository()
        providerConfigRepository.save(ProviderConfig(sttProviderName = providerName))
        return SpeechToTextProviderSelector(
            providerConfigRepository = providerConfigRepository,
            mock = MockSpeechToTextProvider(),
            openAi = openAi,
        )
    }

    @Test
    fun `vychozi (prazdny) provider pouzije mock`() = runTest {
        val result = selector("").transcribe(TranscriptionRequest(audioFile, "cs"))

        assertEquals(TranscriptionResult.Failure.EmptyResult, result)
    }

    @Test
    fun `openai provider deleguje na realnou implementaci`() = runTest {
        val result = selector(ProviderNames.OPENAI).transcribe(TranscriptionRequest(audioFile, "cs"))

        assertEquals(TranscriptionResult.Failure.Unauthorized, result)
    }
}
