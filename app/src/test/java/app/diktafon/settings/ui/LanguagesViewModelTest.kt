package app.diktafon.settings.ui

import app.diktafon.settings.FakeTranscriptionLanguageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LanguagesViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()
    private lateinit var repository: FakeTranscriptionLanguageRepository
    private lateinit var viewModel: LanguagesViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        repository = FakeTranscriptionLanguageRepository(initial = emptyList())
        viewModel = LanguagesViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `pridani a smazani jazyka`() = runTest {
        viewModel.add("cs", "Čeština")
        assertEquals(listOf("cs"), viewModel.languages().first().map { it.code })

        viewModel.remove("cs")

        assertEquals(emptyList<String>(), viewModel.languages().first().map { it.code })
    }

    @Test
    fun `prazdny kod ani nazev se neprida`() = runTest {
        viewModel.add("", "Čeština")
        viewModel.add("cs", "")

        assertEquals(emptyList<String>(), viewModel.languages().first().map { it.code })
    }
}
