package app.diktafon.recording.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.diktafon.recording.domain.Recording
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RecordingRepositoryTest {

    private val db = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        AppDatabase::class.java,
    ).allowMainThreadQueries().build()

    private val repository = RecordingRepositoryImpl(db.recordingDao())

    @Test
    fun `ulozena nahravka se objevi v seznamu vcetne metadat`() = runTest {
        repository.saveRecording(
            Recording(
                name = "Test",
                note = "Poznámka",
                folderId = 7,
                filePath = "/tmp/test.m4a",
                durationMs = 12_345,
                createdAtMillis = 1_700_000_000_000,
            ),
        )

        val all = repository.observeAll().first()

        assertEquals(1, all.size)
        assertEquals("Test", all[0].name)
        assertEquals(7L, all[0].folderId)
        assertEquals(12_345L, all[0].durationMs)
    }
}
