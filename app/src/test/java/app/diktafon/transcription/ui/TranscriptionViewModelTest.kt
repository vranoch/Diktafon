package app.diktafon.transcription.ui

import app.diktafon.recording.FakeClock
import app.diktafon.recovery.FakeProcessingJobRepository
import app.diktafon.settings.FakeTranscriptionLanguageRepository
import app.diktafon.transcription.FakeSpeechToTextProvider
import app.diktafon.transcription.FakeTranscriptRepository
import app.diktafon.transcription.domain.TranscriptionResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
class TranscriptionViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()
    private lateinit var repository: FakeTranscriptRepository
    private val audioFile = File("/tmp/recording.m4a")

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        repository = FakeTranscriptRepository()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun viewModel(result: TranscriptionResult) = TranscriptionViewModel(
        sttProvider = FakeSpeechToTextProvider(result),
        transcriptRepository = repository,
        processingJobRepository = FakeProcessingJobRepository(),
        transcriptionLanguageRepository = FakeTranscriptionLanguageRepository(),
        clock = FakeClock(5_000_000),
    )

    @Test
    fun `nacteni existujiciho prepisu naplni stav bez noveho volani STT`() = runTest {
        repository.saveNewRevision(1, "drivejsi prepis", "cs", "mock", "mock-v1", 500)
        val vm = viewModel(TranscriptionResult.Failure.Unauthorized())

        vm.loadExisting(1)

        val state = vm.uiState.value
        assertEquals(TranscriptionPhase.READY, state.phase)
        assertEquals("drivejsi prepis", state.text)
    }

    @Test
    fun `uspesny prepis nastavi stav READY jako neulozeny navrh cekajici na prijmuti`() {
        val vm = viewModel(
            TranscriptionResult.Success(text = "Ahoj světe", providerName = "mock", modelName = "mock-v1"),
        )

        vm.startTranscription(recordingId = 1, audioFile = audioFile)

        val state = vm.uiState.value
        assertEquals(TranscriptionPhase.READY, state.phase)
        assertEquals("Ahoj světe", state.text)
        assertEquals(true, state.isPendingAccept)
        assertEquals(0, repository.saved.size)
    }

    @Test
    fun `prijmuti prepisu ulozi prvni verzi a oznaci dokonceno`() {
        val vm = viewModel(
            TranscriptionResult.Success(text = "Ahoj světe", providerName = "mock", modelName = "mock-v1"),
        )
        vm.startTranscription(recordingId = 1, audioFile = audioFile)

        vm.acceptTranscription(recordingId = 1)

        val state = vm.uiState.value
        assertEquals("Ahoj světe", state.text)
        assertEquals("Ahoj světe", state.savedText)
        assertEquals(1, state.version)
        assertEquals(false, state.isPendingAccept)
        assertEquals(true, state.isFinished)
        assertEquals(1, repository.saved.size)
    }

    @Test
    fun `odmitnuti cerstveho prepisu nic neulozi a vrati puvodni text`() = runTest {
        repository.saveNewRevision(1, "drivejsi prepis", "cs", "mock", "mock-v1", 500)
        val vm = viewModel(
            TranscriptionResult.Success(text = "Novy prepis", providerName = "mock", modelName = "mock-v1"),
        )
        vm.loadExisting(1)

        vm.startTranscription(recordingId = 1, audioFile = audioFile)
        vm.rejectTranscription()

        val state = vm.uiState.value
        assertEquals("drivejsi prepis", state.text)
        assertEquals(false, state.isPendingAccept)
        assertEquals(true, state.isFinished)
        assertEquals(1, repository.saved.size)
    }

    @Test
    fun `odmitnuti prepisu bez predchozi revize vrati prazdny text`() {
        val vm = viewModel(
            TranscriptionResult.Success(text = "Novy prepis", providerName = "mock", modelName = "mock-v1"),
        )
        vm.startTranscription(recordingId = 1, audioFile = audioFile)

        vm.rejectTranscription()

        assertEquals("", vm.uiState.value.text)
        assertEquals(0, repository.saved.size)
    }

    @Test
    fun `psani do textu samo o sobe nic neuklada`() {
        val vm = viewModel(
            TranscriptionResult.Success(text = "Puvodni text", providerName = "mock", modelName = "mock-v1"),
        )
        vm.startTranscription(recordingId = 7, audioFile = audioFile)

        vm.onTextChanged("Rozepsana zmena")

        assertEquals("Rozepsana zmena", vm.uiState.value.text)
        assertEquals(0, repository.saved.size)
    }

    @Test
    fun `ulozeni upravy jiz prijateho prepisu vytvori novou revizi a zachova predchozi`() {
        val vm = viewModel(
            TranscriptionResult.Success(text = "Puvodni text", providerName = "mock", modelName = "mock-v1"),
        )
        vm.startTranscription(recordingId = 7, audioFile = audioFile)
        vm.acceptTranscription(recordingId = 7)

        vm.onTextChanged("Opraveny text")
        vm.saveTextEdits(recordingId = 7)

        val state = vm.uiState.value
        assertEquals("Opraveny text", state.text)
        assertEquals("Opraveny text", state.savedText)
        assertEquals(2, state.version)
        assertEquals(2, repository.saved.size)
        assertEquals("Puvodni text", repository.saved[0].text)
        assertEquals("Opraveny text", repository.saved[1].text)
    }

    @Test
    fun `zruseni upravy jiz prijateho prepisu vrati puvodni ulozeny text`() {
        val vm = viewModel(
            TranscriptionResult.Success(text = "Puvodni text", providerName = "mock", modelName = "mock-v1"),
        )
        vm.startTranscription(recordingId = 7, audioFile = audioFile)
        vm.acceptTranscription(recordingId = 7)

        vm.onTextChanged("Rozepsana zmena")
        vm.cancelTextEdits()

        assertEquals("Puvodni text", vm.uiState.value.text)
        assertEquals(1, repository.saved.size)
    }

    @Test
    fun `chyba autorizace nastavi stav ERROR se srozumitelnou zpravou`() {
        val vm = viewModel(TranscriptionResult.Failure.Unauthorized())

        vm.startTranscription(recordingId = 1, audioFile = audioFile)

        val state = vm.uiState.value
        assertEquals(TranscriptionPhase.ERROR, state.phase)
        assertNotNull(state.errorMessage)
        assertEquals(0, repository.saved.size)
    }

    @Test
    fun `prazdny prepis lze zopakovat bez ztraty audia`() {
        val vm = viewModel(TranscriptionResult.Failure.EmptyResult)
        vm.startTranscription(recordingId = 1, audioFile = audioFile)
        assertEquals(TranscriptionPhase.ERROR, vm.uiState.value.phase)

        val retryVm = TranscriptionViewModel(
            sttProvider = FakeSpeechToTextProvider(
                TranscriptionResult.Success(text = "Ted uz ano", providerName = "mock", modelName = "mock-v1"),
            ),
            transcriptRepository = repository,
            processingJobRepository = FakeProcessingJobRepository(),
            transcriptionLanguageRepository = FakeTranscriptionLanguageRepository(),
            clock = FakeClock(6_000_000),
        )
        retryVm.startTranscription(recordingId = 1, audioFile = audioFile)

        assertEquals(TranscriptionPhase.READY, retryVm.uiState.value.phase)
        assertEquals("Ted uz ano", retryVm.uiState.value.text)
    }
}
