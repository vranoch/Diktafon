package app.diktafon.structuring.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.diktafon.recording.data.AppDatabase
import app.diktafon.structuring.domain.StructuredDocument
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class StructuredDocumentRepositoryTest {

    private val db = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        AppDatabase::class.java,
    ).allowMainThreadQueries().build()

    private val repository = StructuredDocumentRepositoryImpl(db.structuredDocumentDao())

    @Test
    fun `ulozeny dokument se nactete vcetne poli a chybejicich povinnych poli`() = runTest {
        repository.save(
            StructuredDocument(
                recordingId = 1,
                transcriptId = 1,
                templateId = 5,
                templateVersion = 2,
                fields = mapOf("datum" to "2026-11-13", "ucastnici" to null),
                missingRequiredFields = listOf("ucastnici"),
                createdAtMillis = 1_000,
            ),
        )

        val all = repository.observeForRecording(1).first()

        assertEquals(1, all.size)
        assertEquals("2026-11-13", all[0].fields["datum"])
        assertEquals(listOf("ucastnici"), all[0].missingRequiredFields)
        assertEquals(5L, all[0].templateId)
        assertEquals(2, all[0].templateVersion)
    }
}
