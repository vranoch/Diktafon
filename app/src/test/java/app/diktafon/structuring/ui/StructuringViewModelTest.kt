package app.diktafon.structuring.ui

import app.diktafon.recording.FakeClock
import app.diktafon.structuring.FakeDocumentProcessor
import app.diktafon.structuring.FakeStructuredDocumentRepository
import app.diktafon.structuring.FakeStructuredDocumentValidator
import app.diktafon.structuring.domain.StructuringResult
import app.diktafon.structuring.domain.Template
import app.diktafon.structuring.domain.TemplateField
import app.diktafon.structuring.domain.ValidationOutcome
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StructuringViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()
    private lateinit var repository: FakeStructuredDocumentRepository
    private val template = Template(
        id = 1,
        name = "Zápis",
        version = 1,
        fields = listOf(TemplateField("datum", required = true), TemplateField("ucastnici", required = true)),
        instructions = "",
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        repository = FakeStructuredDocumentRepository()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun viewModel(result: StructuringResult, validation: ValidationOutcome) = StructuringViewModel(
        documentProcessor = FakeDocumentProcessor(result),
        validator = FakeStructuredDocumentValidator(validation),
        repository = repository,
        clock = FakeClock(1_000_000),
    )

    @Test
    fun `uspesne zpracovani vsech poli nastavi READY bez chybejicich poli`() {
        val vm = viewModel(
            result = StructuringResult.Success("""{"datum":"2026-11-13","ucastnici":"Jan"}"""),
            validation = ValidationOutcome.Valid(
                fields = mapOf("datum" to "2026-11-13", "ucastnici" to "Jan"),
                missingRequiredFields = emptyList(),
            ),
        )

        vm.startStructuring(recordingId = 1, transcriptId = 1, transcriptText = "text", template = template)

        val state = vm.uiState.value
        assertEquals(StructuringPhase.READY, state.phase)
        assertEquals("Jan", state.fields["ucastnici"])
        assertEquals(1, repository.saved.size)
    }

    @Test
    fun `chybejici povinne pole se zobrazi jako nejasnost, ne jako chyba`() {
        val vm = viewModel(
            result = StructuringResult.Success("""{"datum":"2026-11-13"}"""),
            validation = ValidationOutcome.Valid(
                fields = mapOf("datum" to "2026-11-13", "ucastnici" to null),
                missingRequiredFields = listOf("ucastnici"),
            ),
        )

        vm.startStructuring(recordingId = 1, transcriptId = 1, transcriptText = "text", template = template)

        val state = vm.uiState.value
        assertEquals(StructuringPhase.READY, state.phase)
        assertEquals(listOf("ucastnici"), state.missingRequiredFields)
        assertEquals(1, repository.saved.size)
    }

    @Test
    fun `nevalidni json nastavi ERROR a nic se neuklada`() {
        val vm = viewModel(
            result = StructuringResult.Success("neplatny json"),
            validation = ValidationOutcome.InvalidJson,
        )

        vm.startStructuring(recordingId = 1, transcriptId = 1, transcriptText = "text", template = template)

        val state = vm.uiState.value
        assertEquals(StructuringPhase.ERROR, state.phase)
        assertNotNull(state.errorMessage)
        assertEquals(0, repository.saved.size)
    }

    @Test
    fun `odmitnuti modelem nastavi ERROR se srozumitelnou zpravou`() {
        val vm = viewModel(
            result = StructuringResult.Failure.RejectedByModel,
            validation = ValidationOutcome.InvalidJson,
        )

        vm.startStructuring(recordingId = 1, transcriptId = 1, transcriptText = "text", template = template)

        val state = vm.uiState.value
        assertEquals(StructuringPhase.ERROR, state.phase)
        assertNotNull(state.errorMessage)
    }

    @Test
    fun `timeout nastavi ERROR`() {
        val vm = viewModel(
            result = StructuringResult.Failure.Timeout,
            validation = ValidationOutcome.InvalidJson,
        )

        vm.startStructuring(recordingId = 1, transcriptId = 1, transcriptText = "text", template = template)

        assertEquals(StructuringPhase.ERROR, vm.uiState.value.phase)
    }
}
