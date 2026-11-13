package app.diktafon.recording.ui

import android.net.Uri
import app.diktafon.folders.FakeLastUsedFolderRepository
import app.diktafon.recording.FakeAudioDurationReader
import app.diktafon.recording.FakeAudioFileImporter
import app.diktafon.recording.FakeAudioSource
import app.diktafon.recording.FakeAudioSourceFactory
import app.diktafon.recording.FakeClock
import app.diktafon.recording.FakeRecordingRepository
import app.diktafon.recording.domain.ImportOutcome
import app.diktafon.recording.domain.NameGenerator
import app.diktafon.recording.domain.RecordingFileFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class ImportViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()
    private lateinit var repository: FakeRecordingRepository
    private val fakeUri: Uri = Uri.parse("content://fake-provider/audio/1")

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        repository = FakeRecordingRepository()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun viewModel(
        importOutcome: ImportOutcome,
        durationMs: Long = 0,
    ): ImportViewModel {
        val destination = File.createTempFile("import-vm-test", ".m4a").apply { deleteOnExit() }
        return ImportViewModel(
            audioSourceFactory = FakeAudioSourceFactory(FakeAudioSource()),
            importer = FakeAudioFileImporter(importOutcome),
            durationReader = FakeAudioDurationReader(durationMs),
            repository = repository,
            nameGenerator = NameGenerator(FakeClock(1_000_000)),
            clock = FakeClock(2_000_000),
            fileFactory = object : RecordingFileFactory {
                override fun create(name: String) = destination
            },
            lastUsedFolderRepository = FakeLastUsedFolderRepository(),
        )
    }

    @Test
    fun `zahajeni importu nastavi predgenerovany nazev a cekani na soubor`() {
        val vm = viewModel(importOutcome = ImportOutcome.Success(File("/tmp/unused.m4a")))

        vm.startNewImport(folderId = 9)

        val state = vm.uiState.value
        assertEquals(ImportPhase.AWAITING_FILE, state.phase)
        assertEquals(9L, state.folderId)
        assertEquals(false, state.name.isBlank())
    }

    @Test
    fun `uspesny import ulozi nahravku s delkou z duration readeru`() {
        val vm = viewModel(importOutcome = ImportOutcome.Success(File("/tmp/x.m4a")), durationMs = 42_000)
        vm.startNewImport()
        vm.onNameChanged("Import ze starého diktafonu")
        vm.onNoteChanged("Externí nahrávka")

        vm.onFileSelected(fakeUri)

        val state = vm.uiState.value
        assertEquals(ImportPhase.IMPORTED, state.phase)
        assertNotNull(state.savedRecordingId)
        val saved = repository.saved.single()
        assertEquals("Import ze starého diktafonu", saved.name)
        assertEquals("Externí nahrávka", saved.note)
        assertEquals(42_000L, saved.durationMs)
    }

    @Test
    fun `nepodporovany format zustane u vyberu souboru s chybou`() {
        val vm = viewModel(importOutcome = ImportOutcome.UnsupportedFormat)
        vm.startNewImport()

        vm.onFileSelected(fakeUri)

        val state = vm.uiState.value
        assertEquals(ImportPhase.AWAITING_FILE, state.phase)
        assertNotNull(state.error)
        assertEquals(0, repository.saved.size)
    }

    @Test
    fun `prilis velky soubor zustane u vyberu souboru s chybou`() {
        val vm = viewModel(importOutcome = ImportOutcome.TooLarge)
        vm.startNewImport()

        vm.onFileSelected(fakeUri)

        assertEquals(ImportPhase.AWAITING_FILE, vm.uiState.value.phase)
        assertNotNull(vm.uiState.value.error)
    }

    @Test
    fun `zruseni vyberu souboru vycisti chybu a nemeni fazi`() {
        val vm = viewModel(importOutcome = ImportOutcome.UnsupportedFormat)
        vm.startNewImport()
        vm.onFileSelected(fakeUri)
        assertNotNull(vm.uiState.value.error)

        vm.onPickerCancelled()

        assertNull(vm.uiState.value.error)
        assertEquals(ImportPhase.AWAITING_FILE, vm.uiState.value.phase)
    }
}
