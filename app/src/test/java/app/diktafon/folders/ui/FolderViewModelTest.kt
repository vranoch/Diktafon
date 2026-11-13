package app.diktafon.folders.ui

import app.diktafon.folders.domain.Folder
import app.diktafon.folders.domain.FolderDeletionResult
import app.diktafon.folders.domain.FolderRepository
import app.diktafon.recording.FakeRecordingRepository
import app.diktafon.recording.domain.Recording
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FolderViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()
    private val foldersFlow = MutableStateFlow(listOf(Folder(1, "Práce")))
    private var deletionResult: FolderDeletionResult = FolderDeletionResult.Deleted

    private val fakeFolderRepository = object : FolderRepository {
        override suspend fun create(name: String) = 0L
        override suspend fun rename(folderId: Long, newName: String) = Unit
        override suspend fun delete(folderId: Long) = deletionResult
        override fun observeAll(): Flow<List<Folder>> = foldersFlow
    }

    private lateinit var recordingRepository: FakeRecordingRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        recordingRepository = FakeRecordingRepository()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun viewModel() = FolderViewModel(fakeFolderRepository, recordingRepository)

    @Test
    fun `nacteni slozek naplni stav`() {
        val vm = viewModel()
        vm.loadFolders()

        assertEquals(listOf(Folder(1, "Práce")), vm.uiState.value.folders)
    }

    @Test
    fun `vyber slozky zobrazi jeji nahravky`() = runTest {
        recordingRepository.saveRecording(
            Recording(id = 1, name = "A", note = "", folderId = 1, filePath = "/tmp/a.m4a", durationMs = 0, createdAtMillis = 0),
        )
        val vm = viewModel()

        vm.selectFolder(1)

        assertEquals(1, vm.uiState.value.recordingsInSelectedFolder.size)
    }

    @Test
    fun `smazani neprazdne slozky nastavi chybovou zpravu`() {
        deletionResult = FolderDeletionResult.NotEmpty
        val vm = viewModel()

        vm.deleteFolder(1)

        assertNotNull(vm.uiState.value.errorMessage)
    }

    @Test
    fun `smazani prazdne slozky nema chybu`() {
        deletionResult = FolderDeletionResult.Deleted
        val vm = viewModel()

        vm.deleteFolder(1)

        assertNull(vm.uiState.value.errorMessage)
    }
}
