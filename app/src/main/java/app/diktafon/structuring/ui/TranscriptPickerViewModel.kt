package app.diktafon.structuring.ui

import androidx.lifecycle.ViewModel
import app.diktafon.recording.domain.Recording
import app.diktafon.recording.domain.RecordingRepository
import app.diktafon.transcription.domain.Transcript
import app.diktafon.transcription.domain.TranscriptRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

data class RecordingWithTranscript(val recording: Recording, val transcript: Transcript)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class TranscriptPickerViewModel @Inject constructor(
    private val recordingRepository: RecordingRepository,
    private val transcriptRepository: TranscriptRepository,
) : ViewModel() {

    // Nabizi jen nahravky, ktere uz maji aspon jeden prepis - draft sablony se testuje na
    // textu prepisu, ne na samotne nahravce (viz FR-TPL-004).
    fun recordingsWithTranscripts(): Flow<List<RecordingWithTranscript>> =
        recordingRepository.observeAll().flatMapLatest { recordings ->
            if (recordings.isEmpty()) {
                flowOf(emptyList())
            } else {
                combine(recordings.map { recording -> transcriptRepository.observeLatest(recording.id).map { recording to it } }) { pairs ->
                    pairs.mapNotNull { (recording, transcript) -> transcript?.let { RecordingWithTranscript(recording, it) } }
                        .sortedByDescending { it.recording.createdAtMillis }
                }
            }
        }
}
