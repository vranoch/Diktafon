package app.diktafon.transcription.ui

import app.diktafon.recording.FakeClock
import app.diktafon.transcription.FakeSpeechToTextProvider
import app.diktafon.transcription.FakeTranscriptRepository
import app.diktafon.transcription.domain.TranscriptionResult
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
        clock = FakeClock(5_000_000),
    )

    @Test
    fun `uspesny prepis ulozi prvni verzi a nastavi stav READY`() {
        val vm = viewModel(
            TranscriptionResult.Success(text = "Ahoj světe", providerName = "mock", modelName = "mock-v1"),
        )

        vm.startTranscription(recordingId = 1, audioFile = audioFile)

        val state = vm.uiState.value
        assertEquals(TranscriptionPhase.READY, state.phase)
        assertEquals("Ahoj světe", state.text)
        assertEquals(1, state.version)
        assertEquals(1, repository.saved.size)
    }

    @Test
    fun `editace prepisu vytvori novou revizi a zachova predchozi`() {
        val vm = viewModel(
            TranscriptionResult.Success(text = "Puvodni text", providerName = "mock", modelName = "mock-v1"),
        )
        vm.startTranscription(recordingId = 7, audioFile = audioFile)

        vm.editTranscript(recordingId = 7, newText = "Opraveny text")

        val state = vm.uiState.value
        assertEquals("Opraveny text", state.text)
        assertEquals(2, state.version)
        assertEquals(2, repository.saved.size)
        assertEquals("Puvodni text", repository.saved[0].text)
        assertEquals("Opraveny text", repository.saved[1].text)
    }

    @Test
    fun `chyba autorizace nastavi stav ERROR se srozumitelnou zpravou`() {
        val vm = viewModel(TranscriptionResult.Failure.Unauthorized)

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
            clock = FakeClock(6_000_000),
        )
        retryVm.startTranscription(recordingId = 1, audioFile = audioFile)

        assertEquals(TranscriptionPhase.READY, retryVm.uiState.value.phase)
        assertEquals("Ted uz ano", retryVm.uiState.value.text)
    }
}
