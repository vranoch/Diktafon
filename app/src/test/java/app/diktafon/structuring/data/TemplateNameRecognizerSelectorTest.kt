package app.diktafon.structuring.data

import app.diktafon.settings.FakeApiKeyRepository
import app.diktafon.settings.FakeProviderConfigRepository
import app.diktafon.settings.domain.ProviderConfig
import app.diktafon.settings.domain.ProviderNames
import app.diktafon.structuring.domain.Template
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class TemplateNameRecognizerSelectorTest {

    private val catalog = listOf(Template(1, "Zápis z porady", 1, emptyList(), ""))

    // Bez ulozeneho klice vraci OpenAiTemplateNameRecognizer deterministicky null, aniz by se
    // pokusil o realne sitove volani - staci k odliseni od mocku v testu.
    private val openAi = OpenAiTemplateNameRecognizer(
        apiKeyRepository = FakeApiKeyRepository(),
        providerConfigRepository = FakeProviderConfigRepository(),
        httpClient = OkHttpClient(),
    )

    private suspend fun selector(providerName: String): TemplateNameRecognizerSelector {
        val providerConfigRepository = FakeProviderConfigRepository()
        providerConfigRepository.save(ProviderConfig(llmProviderName = providerName))
        return TemplateNameRecognizerSelector(
            providerConfigRepository = providerConfigRepository,
            mock = SubstringTemplateNameRecognizer(),
            openAi = openAi,
        )
    }

    @Test
    fun `vychozi (prazdny) provider pouzije mock`() = runTest {
        val result = selector("").recognize("toto je zápis z porady", catalog)

        assertEquals(catalog[0], result)
    }

    @Test
    fun `openai provider deleguje na realnou implementaci`() = runTest {
        val result = selector(ProviderNames.OPENAI).recognize("toto je zápis z porady", catalog)

        assertNull(result)
    }
}
