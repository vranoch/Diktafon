package app.diktafon.folders.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.diktafon.folders.domain.FolderDeletionResult
import app.diktafon.recording.data.AppDatabase
import app.diktafon.recording.data.RecordingEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class FolderRepositoryImplTest {

    private val db = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        AppDatabase::class.java,
    ).allowMainThreadQueries().build()

    private val repository = FolderRepositoryImpl(db.folderDao(), db.recordingDao())

    @Test
    fun `vytvoreni a prejmenovani slozky`() = runTest {
        val id = repository.create("Práce")
        repository.rename(id, "Pracovní poznámky")

        assertEquals(listOf("Pracovní poznámky"), repository.observeAll().first().map { it.name })
    }

    @Test
    fun `prazdnou slozku lze smazat`() = runTest {
        val id = repository.create("Dočasná")

        val result = repository.delete(id)

        assertEquals(FolderDeletionResult.Deleted, result)
        assertEquals(emptyList<String>(), repository.observeAll().first().map { it.name })
    }

    @Test
    fun `neprazdnou slozku nelze smazat`() = runTest {
        val id = repository.create("Obsazená")
        db.recordingDao().insert(
            RecordingEntity(
                name = "Nahrávka",
                note = "",
                folderId = id,
                filePath = "/tmp/a.m4a",
                durationMs = 1_000,
                createdAtMillis = 1_000,
            ),
        )

        val result = repository.delete(id)

        assertEquals(FolderDeletionResult.NotEmpty, result)
        assertEquals(1, repository.observeAll().first().size)
    }
}
