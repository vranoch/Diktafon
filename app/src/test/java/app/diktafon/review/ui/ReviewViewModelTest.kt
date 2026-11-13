package app.diktafon.review.ui

import app.diktafon.structuring.FakeStructuredDocumentRepository
import app.diktafon.structuring.domain.StructuredDocument
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ReviewViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()
    private lateinit var repository: FakeStructuredDocumentRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        repository = FakeStructuredDocumentRepository()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private suspend fun seedDocument(): Long = repository.save(
        StructuredDocument(
            recordingId = 1,
            transcriptId = 1,
            templateId = 1,
            templateVersion = 1,
            fields = mapOf("datum" to "2026-11-13", "ucastnici" to null),
            missingRequiredFields = listOf("ucastnici"),
            createdAtMillis = 1_000,
        ),
    )

    @Test
    fun `nacteni dokumentu naplni stav poli a nejasnostmi`() = runTest {
        val documentId = seedDocument()
        val vm = ReviewViewModel(repository)

        vm.load(documentId, transcriptText = "puvodni prepis")

        val state = vm.uiState.value
        assertEquals("2026-11-13", state.fields["datum"])
        assertEquals(listOf("ucastnici"), state.missingRequiredFields)
        assertFalse(state.isApproved)
        assertEquals("puvodni prepis", state.transcriptText)
    }

    @Test
    fun `oprava pole a ulozeni zmen se promitne do repozitare`() = runTest {
        val documentId = seedDocument()
        val vm = ReviewViewModel(repository)
        vm.load(documentId, "prepis")

        vm.onFieldChanged("ucastnici", "Jan Novák")
        vm.saveChanges()

        val stored = repository.saved.first { it.id == documentId }
        assertEquals("Jan Novák", stored.fields["ucastnici"])
    }

    @Test
    fun `schvaleni ulozi zmeny a nastavi isApproved`() = runTest {
        val documentId = seedDocument()
        val vm = ReviewViewModel(repository)
        vm.load(documentId, "prepis")
        vm.onFieldChanged("ucastnici", "Jan Novák")

        vm.approve()

        val stored = repository.saved.first { it.id == documentId }
        assertTrue(stored.isApproved)
        assertEquals("Jan Novák", stored.fields["ucastnici"])
        assertTrue(vm.uiState.value.isApproved)
    }
}
