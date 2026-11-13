package app.diktafon.deletion.data

import app.diktafon.deletion.domain.DataDeletionRepository
import app.diktafon.deletion.domain.DeletionResult
import app.diktafon.recording.data.RecordingDao
import app.diktafon.structuring.data.StructuredDocumentDao
import app.diktafon.transcription.data.TranscriptDao
import java.io.File
import javax.inject.Inject

class DataDeletionRepositoryImpl @Inject constructor(
    private val recordingDao: RecordingDao,
    private val transcriptDao: TranscriptDao,
    private val structuredDocumentDao: StructuredDocumentDao,
) : DataDeletionRepository {

    override suspend fun deleteRecordingWithAllArtifacts(recordingId: Long): DeletionResult {
        val recording = recordingDao.getById(recordingId)

        transcriptDao.deleteAllForRecording(recordingId)
        structuredDocumentDao.deleteAllForRecording(recordingId)
        recordingDao.deleteById(recordingId)

        val file = recording?.filePath?.let(::File)
        return if (file != null && file.exists() && !file.delete()) {
            DeletionResult.PartialFailure(listOf(file.absolutePath))
        } else {
            DeletionResult.Success
        }
    }

    override suspend fun deleteTranscriptsForRecording(recordingId: Long): DeletionResult {
        transcriptDao.deleteAllForRecording(recordingId)
        return DeletionResult.Success
    }

    override suspend fun deleteStructuredDocumentsForRecording(recordingId: Long): DeletionResult {
        structuredDocumentDao.deleteAllForRecording(recordingId)
        return DeletionResult.Success
    }
}
