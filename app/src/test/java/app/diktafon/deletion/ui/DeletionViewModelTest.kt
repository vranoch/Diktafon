package app.diktafon.deletion.ui

import app.diktafon.deletion.domain.DataDeletionRepository
import app.diktafon.deletion.domain.DeletionResult
import app.diktafon.structuring.FakeStructuredDocumentRepository
import app.diktafon.structuring.domain.StructuredDocument
import app.diktafon.transcription.FakeTranscriptRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DeletionViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()
    private lateinit var transcriptRepository: FakeTranscriptRepository
    private lateinit var structuredDocumentRepository: FakeStructuredDocumentRepository

    private var lastDeletionResult: DeletionResult = DeletionResult.Success
    private val deletionRepository = object : DataDeletionRepository {
        var recordingDeleteCalls = 0
        var transcriptDeleteCalls = 0
        var documentDeleteCalls = 0

        override suspend fun deleteRecordingWithAllArtifacts(recordingId: Long): DeletionResult {
            recordingDeleteCalls++
            return lastDeletionResult
        }

        override suspend fun deleteTranscriptsForRecording(recordingId: Long): DeletionResult {
            transcriptDeleteCalls++
            return lastDeletionResult
        }

        override suspend fun deleteStructuredDocumentsForRecording(recordingId: Long): DeletionResult {
            documentDeleteCalls++
            return lastDeletionResult
        }
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        transcriptRepository = FakeTranscriptRepository()
        structuredDocumentRepository = FakeStructuredDocumentRepository()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun viewModel() = DeletionViewModel(transcriptRepository, structuredDocumentRepository, deletionRepository)

    @Test
    fun `nacteni zavislych artefaktu spocita revize a vysledky`() = runTest {
        structuredDocumentRepository.save(
            StructuredDocument(
                recordingId = 1,
                transcriptId = 1,
                templateId = 1,
                templateVersion = 1,
                fields = emptyMap(),
                missingRequiredFields = emptyList(),
                createdAtMillis = 1_000,
            ),
        )
        transcriptRepository.saveNewRevision(1, "text", "cs", "mock", "mock-v1", 1_000)

        val vm = viewModel()
        vm.loadDependentArtifacts(1)

        assertEquals(1, vm.uiState.value.transcriptRevisionCount)
        assertEquals(1, vm.uiState.value.structuredDocumentCount)
    }

    @Test
    fun `potvrzeni smazani celeho zaznamu nastavi isDeleted`() = runTest {
        val vm = viewModel()
        vm.loadDependentArtifacts(1)

        vm.confirmDeleteRecording()

        assertTrue(vm.uiState.value.isDeleted)
        assertEquals(1, deletionRepository.recordingDeleteCalls)
    }

    @Test
    fun `castecne selhani smazani souboru se promitne jako chybova zprava`() = runTest {
        lastDeletionResult = DeletionResult.PartialFailure(listOf("/tmp/audio.m4a"))
        val vm = viewModel()
        vm.loadDependentArtifacts(1)

        vm.confirmDeleteRecording()

        assertTrue(vm.uiState.value.isDeleted)
        assertTrue(vm.uiState.value.errorMessage!!.contains("/tmp/audio.m4a"))
    }
}
