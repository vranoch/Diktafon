package app.diktafon.settings.ui

import app.diktafon.settings.FakeApiKeyRepository
import app.diktafon.settings.FakeProviderConfigRepository
import app.diktafon.settings.FakeTranscriptionLanguageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ApiConfigViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()
    private lateinit var apiKeyRepository: FakeApiKeyRepository
    private lateinit var providerConfigRepository: FakeProviderConfigRepository
    private lateinit var viewModel: ApiConfigViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        apiKeyRepository = FakeApiKeyRepository()
        providerConfigRepository = FakeProviderConfigRepository()
        viewModel = ApiConfigViewModel(apiKeyRepository, providerConfigRepository, FakeTranscriptionLanguageRepository())
        viewModel.load()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `ulozeni platneho klice nastavi hasSttApiKey`() {
        viewModel.saveSttApiKey("sk-test-123")

        assertTrue(viewModel.uiState.value.hasSttApiKey)
        assertNotNull(viewModel.uiState.value.statusMessage)
    }

    @Test
    fun `prazdny klic se neulozi a zobrazi chybu`() {
        viewModel.saveSttApiKey("")

        assertFalse(viewModel.uiState.value.hasSttApiKey)
        assertNotNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `smazani klice zrusi priznak hasLlmApiKey`() {
        viewModel.saveLlmApiKey("sk-llm-456")
        assertTrue(viewModel.uiState.value.hasLlmApiKey)

        viewModel.clearLlmApiKey()

        assertFalse(viewModel.uiState.value.hasLlmApiKey)
    }

    @Test
    fun `ulozeni konfigurace providera a modelu se projevi ve stavu`() {
        viewModel.saveConfig(
            sttProviderName = "acme",
            sttModel = "stt-1",
            llmProviderName = "acme",
            llmModel = "llm-1",
            defaultLanguageCode = "en",
        )

        val state = viewModel.uiState.value
        assertEquals("acme", state.sttProviderName)
        assertEquals("en", state.defaultLanguageCode)
    }
}
