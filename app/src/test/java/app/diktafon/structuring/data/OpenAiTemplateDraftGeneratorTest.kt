package app.diktafon.structuring.data

import app.diktafon.settings.FakeApiKeyRepository
import app.diktafon.settings.FakeProviderConfigRepository
import app.diktafon.structuring.domain.TemplateDraftRequest
import app.diktafon.structuring.domain.TemplateDraftResult
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.JsonPrimitive
import okhttp3.OkHttpClient
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class OpenAiTemplateDraftGeneratorTest {

    private val generator = OpenAiTemplateDraftGenerator(
        apiKeyRepository = FakeApiKeyRepository(),
        providerConfigRepository = FakeProviderConfigRepository(),
        httpClient = OkHttpClient(),
    )

    private fun chatCompletionResponse(draftJson: String): String = """
        {"choices": [{"message": {"content": ${JsonPrimitive(draftJson)}}}]}
    """.trimIndent()

    @Test
    fun `uspesna odpoved naplni pole a instrukce`() {
        val draftJson = """{"fields": [{"name": "Subjektivní nález", "required": true}, {"name": "Terapie", "required": false}], "instructions": "Vyplň podle vzoru"}"""

        val result = generator.mapResponse(200, chatCompletionResponse(draftJson))

        assertTrue(result is TemplateDraftResult.Success)
        val success = result as TemplateDraftResult.Success
        assertEquals(2, success.fields.size)
        assertEquals("Subjektivní nález", success.fields[0].label)
        assertTrue(success.fields[0].required)
        assertEquals("Terapie", success.fields[1].label)
        assertEquals(false, success.fields[1].required)
        assertEquals("Vyplň podle vzoru", success.instructions)
    }

    @Test
    fun `prazdny seznam poli je InvalidOutput`() {
        val result = generator.mapResponse(200, chatCompletionResponse("""{"fields": [], "instructions": "x"}"""))

        assertEquals(TemplateDraftResult.Failure.InvalidOutput, result)
    }

    @Test
    fun `nevalidni json v odpovedi je InvalidOutput`() {
        val result = generator.mapResponse(200, "neplatny json")

        assertEquals(TemplateDraftResult.Failure.InvalidOutput, result)
    }

    @Test
    fun `401 a 403 znamenaji Unauthorized`() {
        assertEquals(TemplateDraftResult.Failure.Unauthorized(), generator.mapResponse(401, ""))
        assertEquals(TemplateDraftResult.Failure.Unauthorized(), generator.mapResponse(403, ""))
    }

    @Test
    fun `5xx a 429 znamenaji ServerError`() {
        assertTrue(generator.mapResponse(503, "") is TemplateDraftResult.Failure.ServerError)
        assertTrue(generator.mapResponse(429, "") is TemplateDraftResult.Failure.ServerError)
    }

    @Test
    fun `chybejici API klic vrati Unauthorized bez sitoveho volani`() = runTest {
        val result = generator.generateDraft(TemplateDraftRequest(exampleText = "vzor", fieldNotes = ""))

        assertTrue(result is TemplateDraftResult.Failure.Unauthorized)
        assertTrue((result as TemplateDraftResult.Failure.Unauthorized).details?.isNotBlank() == true)
    }
}
