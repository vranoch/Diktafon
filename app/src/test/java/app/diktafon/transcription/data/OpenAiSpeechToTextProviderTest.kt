package app.diktafon.transcription.data

import app.diktafon.settings.FakeApiKeyRepository
import app.diktafon.settings.FakeProviderConfigRepository
import app.diktafon.transcription.domain.TranscriptionRequest
import app.diktafon.transcription.domain.TranscriptionResult
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class OpenAiSpeechToTextProviderTest {

    private val provider = OpenAiSpeechToTextProvider(
        apiKeyRepository = FakeApiKeyRepository(),
        providerConfigRepository = FakeProviderConfigRepository(),
        httpClient = OkHttpClient(),
    )

    @Test
    fun `chybejici API klic vrati Unauthorized bez sitoveho volani`() = runTest {
        val result = provider.transcribe(TranscriptionRequest(File("/tmp/x.m4a"), "cs"))

        assertTrue(result is TranscriptionResult.Failure.Unauthorized)
        assertTrue((result as TranscriptionResult.Failure.Unauthorized).details?.isNotBlank() == true)
    }

    @Test
    fun `uspesna odpoved vrati text`() {
        val result = provider.mapResponse(200, """{"text": "Ahoj svete"}""", "whisper-1")

        assertTrue(result is TranscriptionResult.Success)
        assertEquals("Ahoj svete", (result as TranscriptionResult.Success).text)
        assertEquals("openai", result.providerName)
        assertEquals("whisper-1", result.modelName)
    }

    @Test
    fun `prazdny text v uspesne odpovedi je EmptyResult`() {
        val result = provider.mapResponse(200, """{"text": ""}""", "whisper-1")

        assertEquals(TranscriptionResult.Failure.EmptyResult, result)
    }

    @Test
    fun `nevalidni json v uspesne odpovedi je EmptyResult`() {
        val result = provider.mapResponse(200, "neplatny json", "whisper-1")

        assertEquals(TranscriptionResult.Failure.EmptyResult, result)
    }

    @Test
    fun `401 a 403 znamenaji Unauthorized`() {
        assertEquals(TranscriptionResult.Failure.Unauthorized(), provider.mapResponse(401, "", "whisper-1"))
        assertEquals(TranscriptionResult.Failure.Unauthorized(), provider.mapResponse(403, "", "whisper-1"))
    }

    @Test
    fun `413 znamena FileTooLarge`() {
        assertEquals(TranscriptionResult.Failure.FileTooLarge, provider.mapResponse(413, "", "whisper-1"))
    }

    @Test
    fun `429 znamena RateLimited`() {
        assertEquals(TranscriptionResult.Failure.RateLimited(), provider.mapResponse(429, "", "whisper-1"))
    }

    @Test
    fun `429 s chybovou zpravou od OpenAI ji preda jako details (odliseni rate-limit od vycerpaneho kreditu)`() {
        val body = """{"error": {"message": "You exceeded your current quota", "type": "insufficient_quota"}}"""

        val result = provider.mapResponse(429, body, "whisper-1")

        assertEquals(TranscriptionResult.Failure.RateLimited("You exceeded your current quota"), result)
    }

    @Test
    fun `401 s chybovou zpravou od OpenAI ji preda jako details`() {
        val body = """{"error": {"message": "Incorrect API key provided", "type": "invalid_request_error"}}"""

        val result = provider.mapResponse(401, body, "whisper-1")

        assertEquals(TranscriptionResult.Failure.Unauthorized("Incorrect API key provided"), result)
    }

    @Test
    fun `5xx znamena ServerError`() {
        val result = provider.mapResponse(503, "", "whisper-1")

        assertTrue(result is TranscriptionResult.Failure.ServerError)
    }
}
