package app.diktafon.navigation

import androidx.lifecycle.ViewModel
import app.diktafon.recording.domain.Recording
import app.diktafon.recording.domain.RecordingRepository
import app.diktafon.structuring.domain.StructuredDocument
import app.diktafon.structuring.domain.StructuredDocumentRepository
import app.diktafon.structuring.domain.Template
import app.diktafon.structuring.domain.TemplateRepository
import app.diktafon.transcription.domain.Transcript
import app.diktafon.transcription.domain.TranscriptRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class PipelineLookupViewModel @Inject constructor(
    private val recordingRepository: RecordingRepository,
    private val transcriptRepository: TranscriptRepository,
    private val structuredDocumentRepository: StructuredDocumentRepository,
    private val templateRepository: TemplateRepository,
) : ViewModel() {

    fun recording(id: Long): Flow<Recording?> = recordingRepository.observeById(id)

    fun latestTranscript(recordingId: Long): Flow<Transcript?> = transcriptRepository.observeLatest(recordingId)

    fun document(id: Long): Flow<StructuredDocument?> = structuredDocumentRepository.observeById(id)

    fun latestDocumentForRecording(recordingId: Long): Flow<StructuredDocument?> =
        structuredDocumentRepository.observeForRecording(recordingId).map { it.firstOrNull() }

    fun approvedCatalog(): Flow<List<Template>> = templateRepository.observeApprovedCatalog()
}
