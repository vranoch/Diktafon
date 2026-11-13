package app.diktafon.recovery.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.diktafon.recording.data.AppDatabase
import app.diktafon.recovery.domain.ProcessingJobType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ProcessingJobRepositoryImplTest {

    private val db = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        AppDatabase::class.java,
    ).allowMainThreadQueries().build()

    private val repository = ProcessingJobRepositoryImpl(db.processingJobDao())

    @Test
    fun `nova uloha se objevi jako preruseno, dokud neni oznacena DONE`() = runTest {
        val jobId = repository.markInProgress(1, ProcessingJobType.TRANSCRIPTION, 1_000)

        assertEquals(1, repository.observeInterrupted().first().size)

        repository.markDone(jobId, 2_000)

        assertTrue(repository.observeInterrupted().first().isEmpty())
    }

    @Test
    fun `oznaceni FAILED odstrani ulohu ze seznamu preruseni`() = runTest {
        val jobId = repository.markInProgress(1, ProcessingJobType.STRUCTURING, 1_000)

        repository.markFailed(jobId, 2_000)

        assertTrue(repository.observeInterrupted().first().isEmpty())
    }
}
