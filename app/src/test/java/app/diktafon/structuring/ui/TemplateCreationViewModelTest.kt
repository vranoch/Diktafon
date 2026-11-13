package app.diktafon.structuring.ui

import app.diktafon.recording.FakeClock
import app.diktafon.structuring.FakeDocumentProcessor
import app.diktafon.structuring.FakeStructuredDocumentValidator
import app.diktafon.structuring.FakeTemplateDraftGenerator
import app.diktafon.structuring.FakeTemplateRepository
import app.diktafon.structuring.domain.StructuringResult
import app.diktafon.structuring.domain.TemplateDraftResult
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
class TemplateCreationViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()
    private lateinit var templateRepository: FakeTemplateRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        templateRepository = FakeTemplateRepository()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun viewModel(
        draftResult: TemplateDraftResult,
        structuringResult: StructuringResult = StructuringResult.Success("{}"),
        validationOutcome: ValidationOutcome = ValidationOutcome.Valid(emptyMap(), emptyList()),
    ) = TemplateCreationViewModel(
        draftGenerator = FakeTemplateDraftGenerator(draftResult),
        documentProcessor = FakeDocumentProcessor(structuringResult),
        validator = FakeStructuredDocumentValidator(validationOutcome),
        templateRepository = templateRepository,
        clock = FakeClock(1_000_000),
    )

    @Test
    fun `odvozeni draftu naplni pole a instrukce`() {
        val vm = viewModel(
            draftResult = TemplateDraftResult.Success(
                fields = listOf(TemplateField("datum", true)),
                instructions = "vypln",
            ),
        )
        vm.onExampleTextChanged("Datum: 2026-11-13")

        vm.generateDraft()

        val state = vm.uiState.value
        assertEquals(listOf(TemplateField("datum", true)), state.draftFields)
        assertEquals("vypln", state.draftInstructions)
    }

    @Test
    fun `test draftu na prepisu naplni vysledek a nejasnosti`() {
        val vm = viewModel(
            draftResult = TemplateDraftResult.Success(listOf(TemplateField("datum", true)), "vypln"),
            validationOutcome = ValidationOutcome.Valid(
                fields = mapOf("datum" to "2026-11-13"),
                missingRequiredFields = emptyList(),
            ),
        )
        vm.generateDraft()

        vm.testDraftOnTranscript("nejaky prepis")

        assertEquals("2026-11-13", vm.uiState.value.testResultFields["datum"])
    }

    @Test
    fun `ulozeni jako nova verze zapise do repozitare`() {
        val vm = viewModel(
            draftResult = TemplateDraftResult.Success(listOf(TemplateField("datum", true)), "vypln"),
        )
        vm.onDraftNameChanged("Zápis z porady")
        vm.generateDraft()

        vm.saveAsNewVersion()

        assertEquals(1, templateRepository.saved.size)
        assertNotNull(vm.uiState.value.savedVersion)
    }

    @Test
    fun `neuspesne odvozeni draftu nastavi chybovou zpravu`() {
        val vm = viewModel(draftResult = TemplateDraftResult.Failure.InvalidOutput)

        vm.generateDraft()

        assertNotNull(vm.uiState.value.errorMessage)
        assertEquals(0, vm.uiState.value.draftFields.size)
    }
}
