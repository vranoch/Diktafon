package app.diktafon.structuring.data

import app.diktafon.settings.FakeApiKeyRepository
import app.diktafon.settings.FakeProviderConfigRepository
import app.diktafon.settings.domain.ProviderConfig
import app.diktafon.settings.domain.ProviderNames
import app.diktafon.structuring.domain.StructuringRequest
import app.diktafon.structuring.domain.StructuringResult
import app.diktafon.structuring.domain.Template
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import org.junit.Assert.assertTrue
import org.junit.Test

class DocumentProcessorSelectorTest {

    private val template = Template(id = 1, name = "T", version = 1, fields = emptyList(), instructions = "")
    private val request = StructuringRequest(transcriptText = "prepis", template = template)

    // Bez ulozeneho klice vraci OpenAiDocumentProcessor deterministicky Unauthorized, aniz by
    // se pokusil o realne sitove volani - staci k odliseni od mocku v testu.
    private val openAi = OpenAiDocumentProcessor(
        apiKeyRepository = FakeApiKeyRepository(),
        providerConfigRepository = FakeProviderConfigRepository(),
        httpClient = OkHttpClient(),
    )

    private suspend fun selector(providerName: String): DocumentProcessorSelector {
        val providerConfigRepository = FakeProviderConfigRepository()
        providerConfigRepository.save(ProviderConfig(llmProviderName = providerName))
        return DocumentProcessorSelector(
            providerConfigRepository = providerConfigRepository,
            mock = MockDocumentProcessor(),
            openAi = openAi,
        )
    }

    @Test
    fun `vychozi (prazdny) provider pouzije mock`() = runTest {
        val result = selector("").structure(request)

        assertTrue(result is StructuringResult.Success)
    }

    @Test
    fun `openai provider deleguje na realnou implementaci`() = runTest {
        val result = selector(ProviderNames.OPENAI).structure(request)

        assertTrue(result == StructuringResult.Failure.Unauthorized)
    }
}
