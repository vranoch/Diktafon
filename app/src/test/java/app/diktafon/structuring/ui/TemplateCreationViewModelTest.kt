package app.diktafon.structuring.ui

import app.diktafon.recording.FakeClock
import app.diktafon.structuring.FakeDocumentProcessor
import app.diktafon.structuring.FakeStructuredDocumentValidator
import app.diktafon.structuring.FakeTemplateDraftGenerator
import app.diktafon.structuring.FakeTemplateRepository
import app.diktafon.structuring.domain.StructuringResult
import app.diktafon.structuring.domain.Template
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
import org.junit.Assert.assertNotEquals
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
    fun `nacteni existujici sablony naplni draft a ulozeni zversuje puvodni id`() {
        val vm = viewModel(draftResult = TemplateDraftResult.Failure.InvalidOutput)
        val existing = Template(
            id = 7,
            name = "Zápis z porady",
            version = 2,
            fields = listOf(TemplateField("datum", true)),
            instructions = "stare instrukce",
            exampleText = "stary vzor",
        )

        vm.loadExisting(existing)
        vm.saveAsNewVersion()

        assertEquals(1, templateRepository.saved.size)
        assertEquals(7L, templateRepository.saved[0].id)
        assertEquals("Zápis z porady", templateRepository.saved[0].name)
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
    fun `prepsani stavajici verze nevytvori novou verzi`() {
        val vm = viewModel(draftResult = TemplateDraftResult.Failure.InvalidOutput)
        val existing = Template(
            id = 7,
            name = "Zápis z porady",
            version = 2,
            fields = listOf(TemplateField("datum", true)),
            instructions = "stare instrukce",
            exampleText = "stary vzor",
        )
        vm.loadExisting(existing)

        vm.overwriteExistingVersion()

        assertEquals(1, templateRepository.saved.size)
        assertEquals(7L, templateRepository.saved[0].id)
        assertEquals(2, templateRepository.saved[0].version)
    }

    @Test
    fun `ulozeni jako nova sablona zalozi novy templateId i pri editaci existujici`() {
        val vm = viewModel(draftResult = TemplateDraftResult.Failure.InvalidOutput)
        val existing = Template(
            id = 7,
            name = "Zápis z porady",
            version = 2,
            fields = listOf(TemplateField("datum", true)),
            instructions = "stare instrukce",
            exampleText = "stary vzor",
        )
        vm.loadExisting(existing)

        vm.saveAsNewTemplate()

        assertEquals(1, templateRepository.saved.size)
        assertNotEquals(7L, templateRepository.saved[0].id)
        assertEquals(1, templateRepository.saved[0].version)
        assertEquals(templateRepository.saved[0].id, vm.uiState.value.editingTemplateId)
    }

    @Test
    fun `neuspesne odvozeni draftu nastavi chybovou zpravu`() {
        val vm = viewModel(draftResult = TemplateDraftResult.Failure.InvalidOutput)

        vm.generateDraft()

        assertNotNull(vm.uiState.value.errorMessage)
        assertEquals(0, vm.uiState.value.draftFields.size)
    }
}
