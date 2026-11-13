package app.diktafon.recording.ui

import app.diktafon.folders.FakeLastUsedFolderRepository
import app.diktafon.recording.FakeAudioRecorder
import app.diktafon.recording.FakeClock
import app.diktafon.recording.FakeRecordingRepository
import app.diktafon.recording.domain.NameGenerator
import app.diktafon.recording.domain.RecordingFileFactory
import app.diktafon.recording.domain.RecordingResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
class RecordingViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()

    private lateinit var audioRecorder: FakeAudioRecorder
    private lateinit var repository: FakeRecordingRepository
    private lateinit var viewModel: RecordingViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        audioRecorder = FakeAudioRecorder()
        repository = FakeRecordingRepository()
        viewModel = RecordingViewModel(
            audioRecorder = audioRecorder,
            repository = repository,
            nameGenerator = NameGenerator(FakeClock(1_000_000)),
            clock = FakeClock(2_000_000),
            fileFactory = object : RecordingFileFactory {
                override fun create(name: String) = File("/tmp/$name.m4a")
            },
            lastUsedFolderRepository = FakeLastUsedFolderRepository(),
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `zahajeni nahravani nastavi stav RECORDING a predgenerovany nazev`() {
        viewModel.startNewRecording()

        val state = viewModel.uiState.value
        assertEquals(RecordingPhase.RECORDING, state.phase)
        assertTrue(state.name.isNotBlank())
        assertTrue(audioRecorder.started)
    }

    @Test
    fun `selhani startu nahravani (napr odmitnute opravneni) nastavi chybu a nespadne`() {
        audioRecorder.startException = SecurityException("RECORD_AUDIO not granted")

        viewModel.startNewRecording()

        val state = viewModel.uiState.value
        assertEquals(RecordingPhase.IDLE, state.phase)
        assertTrue(state.error != null)
    }

    @Test
    fun `pauza a pokracovani se mohou opakovat`() {
        viewModel.startNewRecording()

        viewModel.pause()
        assertEquals(RecordingPhase.PAUSED, viewModel.uiState.value.phase)

        viewModel.resume()
        assertEquals(RecordingPhase.RECORDING, viewModel.uiState.value.phase)

        viewModel.pause()
        viewModel.resume()

        assertEquals(RecordingPhase.RECORDING, viewModel.uiState.value.phase)
        assertEquals(2, audioRecorder.pauseCount)
        assertEquals(2, audioRecorder.resumeCount)
    }

    @Test
    fun `zmena nazvu a poznamky se promitne do stavu`() {
        viewModel.startNewRecording()

        viewModel.onNameChanged("Moje nahrávka")
        viewModel.onNoteChanged("Důležitá poznámka")

        val state = viewModel.uiState.value
        assertEquals("Moje nahrávka", state.name)
        assertEquals("Důležitá poznámka", state.note)
    }

    @Test
    fun `stop ulozi nahravku s nazvem poznamkou slozkou a delkou`() {
        viewModel.startNewRecording(folderId = 42)
        viewModel.onNameChanged("Porada")
        viewModel.onNoteChanged("Zápis z porady")
        audioRecorder.nextStopResult = RecordingResult(File("/tmp/x.m4a"), durationMs = 5_000)

        viewModel.stop()

        val state = viewModel.uiState.value
        assertEquals(RecordingPhase.STOPPED, state.phase)
        assertNotNull(state.savedRecordingId)

        val saved = repository.saved.single()
        assertEquals("Porada", saved.name)
        assertEquals("Zápis z porady", saved.note)
        assertEquals(42L, saved.folderId)
        assertEquals(5_000L, saved.durationMs)
    }
}
