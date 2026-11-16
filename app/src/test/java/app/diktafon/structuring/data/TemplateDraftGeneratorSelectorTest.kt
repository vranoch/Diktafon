package app.diktafon.structuring.data

import app.diktafon.settings.FakeApiKeyRepository
import app.diktafon.settings.FakeProviderConfigRepository
import app.diktafon.settings.domain.ProviderConfig
import app.diktafon.settings.domain.ProviderNames
import app.diktafon.structuring.domain.TemplateDraftRequest
import app.diktafon.structuring.domain.TemplateDraftResult
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import org.junit.Assert.assertTrue
import org.junit.Test

class TemplateDraftGeneratorSelectorTest {

    private val request = TemplateDraftRequest(exampleText = "Subj.: popis\nObj.: nalez", fieldNotes = "")

    // Bez ulozeneho klice vraci OpenAiTemplateDraftGenerator deterministicky Unauthorized, aniz
    // by se pokusil o realne sitove volani - staci k odliseni od mocku v testu.
    private val openAi = OpenAiTemplateDraftGenerator(
        apiKeyRepository = FakeApiKeyRepository(),
        providerConfigRepository = FakeProviderConfigRepository(),
        httpClient = OkHttpClient(),
    )

    private suspend fun selector(providerName: String): TemplateDraftGeneratorSelector {
        val providerConfigRepository = FakeProviderConfigRepository()
        providerConfigRepository.save(ProviderConfig(llmProviderName = providerName))
        return TemplateDraftGeneratorSelector(
            providerConfigRepository = providerConfigRepository,
            mock = MockTemplateDraftGenerator(),
            openAi = openAi,
        )
    }

    @Test
    fun `vychozi (prazdny) provider pouzije mock`() = runTest {
        val result = selector("").generateDraft(request)

        assertTrue(result is TemplateDraftResult.Success)
    }

    @Test
    fun `openai provider deleguje na realnou implementaci`() = runTest {
        val result = selector(ProviderNames.OPENAI).generateDraft(request)

        assertTrue(result is TemplateDraftResult.Failure.Unauthorized)
    }
}
