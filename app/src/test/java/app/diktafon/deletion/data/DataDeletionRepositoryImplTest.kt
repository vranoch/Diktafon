package app.diktafon.deletion.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.diktafon.deletion.domain.DeletionResult
import app.diktafon.recording.data.AppDatabase
import app.diktafon.recording.data.RecordingEntity
import app.diktafon.structuring.data.StructuredDocumentEntity
import app.diktafon.transcription.data.TranscriptEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.File

@RunWith(RobolectricTestRunner::class)
class DataDeletionRepositoryImplTest {

    private val db = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        AppDatabase::class.java,
    ).allowMainThreadQueries().build()

    private val repository = DataDeletionRepositoryImpl(
        recordingDao = db.recordingDao(),
        transcriptDao = db.transcriptDao(),
        structuredDocumentDao = db.structuredDocumentDao(),
    )

    private suspend fun seedRecordingWithArtifacts(audioFile: File): Long {
        val recordingId = db.recordingDao().insert(
            RecordingEntity(
                name = "Test",
                note = "",
                folderId = null,
                filePath = audioFile.absolutePath,
                durationMs = 1_000,
                createdAtMillis = 1_000,
            ),
        )
        db.transcriptDao().insert(
            TranscriptEntity(
                recordingId = recordingId,
                text = "prepis",
                languageCode = "cs",
                providerName = "mock",
                modelName = "mock-v1",
                version = 1,
                createdAtMillis = 1_000,
            ),
        )
        db.structuredDocumentDao().insert(
            StructuredDocumentEntity(
                recordingId = recordingId,
                transcriptId = 1,
                templateId = 1,
                templateVersion = 1,
                fieldsJson = "{}",
                missingFieldsJson = "[]",
                createdAtMillis = 1_000,
            ),
        )
        return recordingId
    }

    @Test
    fun `smazani celeho zaznamu odstrani nahravku, prepisy, vysledky i soubor`() = runTest {
        val audioFile = File.createTempFile("delete-test", ".m4a").apply { writeText("audio") }
        val recordingId = seedRecordingWithArtifacts(audioFile)

        val result = repository.deleteRecordingWithAllArtifacts(recordingId)

        assertEquals(DeletionResult.Success, result)
        assertEquals(null, db.recordingDao().getById(recordingId))
        assertTrue(db.transcriptDao().observeAllRevisions(recordingId).first().isEmpty())
        assertTrue(db.structuredDocumentDao().observeForRecording(recordingId).first().isEmpty())
        assertFalse(audioFile.exists())
    }

    @Test
    fun `smazani jen prepisu zachova nahravku i vysledek`() = runTest {
        val audioFile = File.createTempFile("delete-test-2", ".m4a").apply { writeText("audio") }
        val recordingId = seedRecordingWithArtifacts(audioFile)

        repository.deleteTranscriptsForRecording(recordingId)

        assertTrue(db.transcriptDao().observeAllRevisions(recordingId).first().isEmpty())
        assertEquals(1, db.structuredDocumentDao().observeForRecording(recordingId).first().size)
        assertEquals(recordingId, db.recordingDao().getById(recordingId)?.id)
    }

    @Test
    fun `smazani jen vysledku zachova nahravku i prepis`() = runTest {
        val audioFile = File.createTempFile("delete-test-3", ".m4a").apply { writeText("audio") }
        val recordingId = seedRecordingWithArtifacts(audioFile)

        repository.deleteStructuredDocumentsForRecording(recordingId)

        assertTrue(db.structuredDocumentDao().observeForRecording(recordingId).first().isEmpty())
        assertEquals(1, db.transcriptDao().observeAllRevisions(recordingId).first().size)
        assertEquals(recordingId, db.recordingDao().getById(recordingId)?.id)
    }
}
