package app.diktafon.structuring.data

import app.diktafon.settings.FakeApiKeyRepository
import app.diktafon.settings.FakeProviderConfigRepository
import app.diktafon.structuring.domain.StructuringResult
import okhttp3.OkHttpClient
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class OpenAiDocumentProcessorTest {

    private val processor = OpenAiDocumentProcessor(
        apiKeyRepository = FakeApiKeyRepository(),
        providerConfigRepository = FakeProviderConfigRepository(),
        httpClient = OkHttpClient(),
    )

    private fun chatCompletionResponse(content: String): String = """
        {"choices": [{"message": {"content": ${kotlinx.serialization.json.JsonPrimitive(content)}}}]}
    """.trimIndent()

    @Test
    fun `uspesna odpoved vrati obsah zpravy jako rawJson`() {
        val result = processor.mapResponse(200, chatCompletionResponse("""{"datum": "2026-11-15"}"""))

        assertTrue(result is StructuringResult.Success)
        assertEquals("""{"datum": "2026-11-15"}""", (result as StructuringResult.Success).rawJson)
    }

    @Test
    fun `prazdny obsah zpravy je RejectedByModel`() {
        val result = processor.mapResponse(200, chatCompletionResponse(""))

        assertEquals(StructuringResult.Failure.RejectedByModel, result)
    }

    @Test
    fun `nevalidni json v odpovedi je RejectedByModel`() {
        val result = processor.mapResponse(200, "neplatny json")

        assertEquals(StructuringResult.Failure.RejectedByModel, result)
    }

    @Test
    fun `401 a 403 znamenaji Unauthorized`() {
        assertEquals(StructuringResult.Failure.Unauthorized, processor.mapResponse(401, ""))
        assertEquals(StructuringResult.Failure.Unauthorized, processor.mapResponse(403, ""))
    }

    @Test
    fun `5xx a 429 znamenaji ServerError`() {
        assertTrue(processor.mapResponse(503, "") is StructuringResult.Failure.ServerError)
        assertTrue(processor.mapResponse(429, "") is StructuringResult.Failure.ServerError)
    }
}
