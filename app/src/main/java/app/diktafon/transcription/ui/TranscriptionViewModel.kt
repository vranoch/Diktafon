package app.diktafon.transcription.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.diktafon.recording.domain.Clock
import app.diktafon.recovery.domain.ProcessingJobRepository
import app.diktafon.recovery.domain.ProcessingJobType
import app.diktafon.settings.domain.TranscriptionLanguage
import app.diktafon.settings.domain.TranscriptionLanguageRepository
import app.diktafon.transcription.domain.SpeechToTextProvider
import app.diktafon.transcription.domain.TranscriptRepository
import app.diktafon.transcription.domain.TranscriptionRequest
import app.diktafon.transcription.domain.TranscriptionResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class TranscriptionViewModel @Inject constructor(
    private val sttProvider: SpeechToTextProvider,
    private val transcriptRepository: TranscriptRepository,
    private val processingJobRepository: ProcessingJobRepository,
    private val transcriptionLanguageRepository: TranscriptionLanguageRepository,
    private val clock: Clock,
) : ViewModel() {

    fun availableLanguages(): Flow<List<TranscriptionLanguage>> = transcriptionLanguageRepository.observeAll()

    private val _uiState = MutableStateFlow(TranscriptionUiState())
    val uiState: StateFlow<TranscriptionUiState> = _uiState.asStateFlow()

    fun onLanguageSelected(languageCode: String) {
        _uiState.update { it.copy(languageCode = languageCode) }
    }

    fun loadExisting(recordingId: Long) {
        viewModelScope.launch {
            transcriptRepository.observeLatest(recordingId).collect { transcript ->
                if (transcript != null && _uiState.value.phase != TranscriptionPhase.TRANSCRIBING) {
                    _uiState.update {
                        it.copy(
                            phase = TranscriptionPhase.READY,
                            text = transcript.text,
                            savedText = transcript.text,
                            languageCode = transcript.languageCode,
                            providerName = transcript.providerName,
                            modelName = transcript.modelName,
                            version = transcript.version,
                        )
                    }
                }
            }
        }
    }

    fun startTranscription(recordingId: Long, audioFile: File) {
        if (_uiState.value.phase == TranscriptionPhase.TRANSCRIBING) return
        val languageCode = _uiState.value.languageCode
        _uiState.update { it.copy(phase = TranscriptionPhase.TRANSCRIBING, errorMessage = null) }

        viewModelScope.launch {
            val jobId = processingJobRepository.markInProgress(
                recordingId,
                ProcessingJobType.TRANSCRIPTION,
                clock.nowMillis(),
            )
            when (val result = sttProvider.transcribe(TranscriptionRequest(audioFile, languageCode))) {
                is TranscriptionResult.Success -> {
                    processingJobRepository.markDone(jobId, clock.nowMillis())
                    // Vysledek se jeste neuklada jako nova revize - ceka na prijmuti/odmitnuti
                    // uzivatelem (acceptTranscription/rejectTranscription). savedText zustava
                    // beze zmeny (puvodni ulozeny prepis, nebo "" pokud zadny jeste nebyl).
                    _uiState.update {
                        it.copy(
                            phase = TranscriptionPhase.READY,
                            text = result.text,
                            providerName = result.providerName,
                            modelName = result.modelName,
                            isPendingAccept = true,
                        )
                    }
                }
                is TranscriptionResult.Failure -> {
                    processingJobRepository.markFailed(jobId, clock.nowMillis())
                    _uiState.update {
                        it.copy(phase = TranscriptionPhase.ERROR, errorMessage = result.toUserMessage())
                    }
                }
            }
        }
    }

    // Jen aktualizuje rozpracovany text v UI, nic neuklada - viz saveTextEdits/cancelTextEdits.
    fun onTextChanged(newText: String) {
        _uiState.update { it.copy(text = newText) }
    }

    // Ulozi cerstvy (dosud neulozeny) prepis jako novou revizi a vrati uzivatele na
    // predchozi obrazovku (viz isFinished v TranscriptionUiState).
    fun acceptTranscription(recordingId: Long) {
        val state = _uiState.value
        if (!state.isPendingAccept) return

        viewModelScope.launch {
            val saved = transcriptRepository.saveNewRevision(
                recordingId = recordingId,
                text = state.text,
                languageCode = state.languageCode,
                providerName = state.providerName ?: "neznámý",
                modelName = state.modelName ?: "neznámý",
                createdAtMillis = clock.nowMillis(),
            )
            _uiState.update {
                it.copy(
                    text = saved.text,
                    savedText = saved.text,
                    version = saved.version,
                    isPendingAccept = false,
                    isFinished = true,
                )
            }
        }
    }

    // Zahodi cerstvy (dosud neulozeny) prepis beze zmeny ulozenych dat a vrati uzivatele na
    // predchozi obrazovku - text se vrati na puvodni ulozenou hodnotu (nebo prazdnou, pokud
    // zadna predchozi revize neexistovala).
    fun rejectTranscription() {
        val state = _uiState.value
        if (!state.isPendingAccept) return

        _uiState.update {
            it.copy(text = it.savedText, isPendingAccept = false, isFinished = true)
        }
    }

    fun saveTextEdits(recordingId: Long) {
        val state = _uiState.value
        if (state.phase != TranscriptionPhase.READY || state.text == state.savedText) return

        viewModelScope.launch {
            val saved = transcriptRepository.saveNewRevision(
                recordingId = recordingId,
                text = state.text,
                languageCode = state.languageCode,
                providerName = state.providerName ?: "manual",
                modelName = state.modelName ?: "manual-edit",
                createdAtMillis = clock.nowMillis(),
            )
            _uiState.update { it.copy(text = saved.text, savedText = saved.text, version = saved.version) }
        }
    }

    fun cancelTextEdits() {
        _uiState.update { it.copy(text = it.savedText) }
    }
}

private fun TranscriptionResult.Failure.toUserMessage(): String = when (this) {
    is TranscriptionResult.Failure.Unauthorized ->
        "Neplatné nebo odmítnuté API přihlášení." + details.asSuffix()
    is TranscriptionResult.Failure.RateLimited ->
        "Překročen limit volání STT služby (nebo došel kredit u poskytovatele), zkuste to později." + details.asSuffix()
    TranscriptionResult.Failure.FileTooLarge -> "Soubor je pro STT službu příliš velký."
    TranscriptionResult.Failure.Timeout -> "Vypršel časový limit volání STT služby."
    is TranscriptionResult.Failure.ServerError -> "Chyba STT služby, zkuste to znovu."
    TranscriptionResult.Failure.EmptyResult -> "Přepis vyšel prázdný nebo neplatný, zkuste to znovu."
}

private fun String?.asSuffix(): String = if (this.isNullOrBlank()) "" else " Důvod od poskytovatele: $this"
