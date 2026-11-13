package app.diktafon.transcription.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.diktafon.recording.data.AppDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TranscriptRepositoryTest {

    private val db = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        AppDatabase::class.java,
    ).allowMainThreadQueries().build()

    private val repository = TranscriptRepositoryImpl(db.transcriptDao())

    @Test
    fun `druha revize ma vyssi verzi a prvni zustane dostupna`() = runTest {
        repository.saveNewRevision(
            recordingId = 1,
            text = "Prvni verze",
            languageCode = "cs",
            providerName = "mock",
            modelName = "mock-v1",
            createdAtMillis = 1_000,
        )
        repository.saveNewRevision(
            recordingId = 1,
            text = "Druha verze",
            languageCode = "cs",
            providerName = "manual",
            modelName = "manual-edit",
            createdAtMillis = 2_000,
        )

        val latest = repository.observeLatest(1).first()
        val all = repository.observeAllRevisions(1).first()

        assertEquals("Druha verze", latest?.text)
        assertEquals(2, latest?.version)
        assertEquals(2, all.size)
        assertEquals("Prvni verze", all.last().text)
    }
}
