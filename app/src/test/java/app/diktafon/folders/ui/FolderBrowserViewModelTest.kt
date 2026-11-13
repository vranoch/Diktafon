package app.diktafon.folders.ui

import app.diktafon.folders.FakeFolderRepository
import app.diktafon.recording.FakeRecordingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class FolderBrowserViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()
    private lateinit var folderRepository: FakeFolderRepository
    private lateinit var viewModel: FolderBrowserViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        folderRepository = FakeFolderRepository()
        viewModel = FolderBrowserViewModel(folderRepository, FakeRecordingRepository())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `vytvoreni slozky v aktualnim rodici`() = runTest {
        viewModel.createFolder("Práce", parentFolderId = null)

        assertEquals(listOf("Práce"), folderRepository.folders.map { it.name })
    }

    @Test
    fun `smazani neprazdne slozky nastavi chybovou zpravu`() = runTest {
        val parentId = folderRepository.create("Rodič", null)
        folderRepository.create("Dítě", parentId)

        viewModel.deleteFolder(parentId)

        assertNotNull(viewModel.uiState.value.errorMessage)
    }

    @Test
    fun `presun slozky - zacit, potvrdit, vysledny rodic se zmeni`() = runTest {
        val a = folderRepository.create("A", null)
        val b = folderRepository.create("B", null)

        viewModel.beginMove(b)
        assertEquals(b, viewModel.uiState.value.folderPendingMove)

        viewModel.confirmMoveHere(a)

        assertNull(viewModel.uiState.value.folderPendingMove)
        assertEquals(a, folderRepository.folders.first { it.id == b }.parentFolderId)
    }

    @Test
    fun `zruseni presunu nic nezmeni`() = runTest {
        val b = folderRepository.create("B", null)
        viewModel.beginMove(b)

        viewModel.cancelMove()

        assertNull(viewModel.uiState.value.folderPendingMove)
        assertNull(folderRepository.folders.first { it.id == b }.parentFolderId)
    }
}
