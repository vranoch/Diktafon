package app.diktafon.structuring.data

import app.diktafon.settings.FakeApiKeyRepository
import app.diktafon.settings.FakeProviderConfigRepository
import app.diktafon.structuring.domain.Template
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.JsonPrimitive
import okhttp3.OkHttpClient
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class OpenAiTemplateNameRecognizerTest {

    private val catalog = listOf(
        Template(1, "Zápis z porady", 1, emptyList(), ""),
        Template(2, "Lékařská zpráva", 1, emptyList(), ""),
    )

    private val recognizer = OpenAiTemplateNameRecognizer(
        apiKeyRepository = FakeApiKeyRepository(),
        providerConfigRepository = FakeProviderConfigRepository(),
        httpClient = OkHttpClient(),
    )

    private fun chatCompletionResponse(content: String): String = """
        {"choices": [{"message": {"content": ${JsonPrimitive(content)}}}]}
    """.trimIndent()

    @Test
    fun `platne id z odpovedi se dohleda v katalogu`() {
        val result = recognizer.mapResponse(200, chatCompletionResponse("""{"templateId": 2}"""), catalog)

        assertEquals(catalog[1], result)
    }

    @Test
    fun `null v odpovedi znamena zadnou shodu`() {
        val result = recognizer.mapResponse(200, chatCompletionResponse("""{"templateId": null}"""), catalog)

        assertNull(result)
    }

    @Test
    fun `id mimo katalog se nikdy nevymysli jako sablona`() {
        val result = recognizer.mapResponse(200, chatCompletionResponse("""{"templateId": 999}"""), catalog)

        assertNull(result)
    }

    @Test
    fun `nevalidni json v odpovedi vrati null`() {
        val result = recognizer.mapResponse(200, "neplatny json", catalog)

        assertNull(result)
    }

    @Test
    fun `chybova HTTP odpoved vrati null`() {
        assertNull(recognizer.mapResponse(401, "", catalog))
        assertNull(recognizer.mapResponse(500, "", catalog))
    }

    @Test
    fun `chybejici API klic vrati null bez sitoveho volani`() = runTest {
        val result = recognizer.recognize("zápis z porady", catalog)

        assertNull(result)
    }

    @Test
    fun `prazdny katalog vrati null bez volani`() = runTest {
        val result = recognizer.recognize("zápis z porady", emptyList())

        assertNull(result)
    }
}
