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
    fun `slozka vytvorena v rootu se objevi mezi detmi root urovne`() = runTest {
        repository.create("Práce", parentFolderId = null)

        assertEquals(listOf("Práce"), repository.observeChildren(null).first().map { it.name })
    }

    @Test
    fun `podslozka se objevi jen pod svym rodicem, ne v rootu`() = runTest {
        val parentId = repository.create("Práce", parentFolderId = null)
        repository.create("Projekt A", parentFolderId = parentId)

        assertEquals(1, repository.observeChildren(null).first().size)
        assertEquals(listOf("Projekt A"), repository.observeChildren(parentId).first().map { it.name })
    }

    @Test
    fun `prejmenovani a presun slozky`() = runTest {
        val a = repository.create("A", parentFolderId = null)
        val b = repository.create("B", parentFolderId = null)
        repository.rename(a, "A upraveno")

        repository.move(b, a)

        assertEquals(listOf("A upraveno"), repository.observeChildren(null).first().map { it.name })
        assertEquals(listOf("B"), repository.observeChildren(a).first().map { it.name })
    }

    @Test
    fun `prazdnou slozku lze smazat`() = runTest {
        val id = repository.create("Dočasná", parentFolderId = null)

        assertEquals(FolderDeletionResult.Deleted, repository.delete(id))
    }

    @Test
    fun `slozku s podslozkou nelze smazat`() = runTest {
        val parentId = repository.create("Rodič", parentFolderId = null)
        repository.create("Dítě", parentFolderId = parentId)

        assertEquals(FolderDeletionResult.NotEmpty, repository.delete(parentId))
    }

    @Test
    fun `slozku s nahravkou nelze smazat`() = runTest {
        val id = repository.create("Obsazená", parentFolderId = null)
        db.recordingDao().insert(
            RecordingEntity(name = "N", note = "", folderId = id, filePath = "/tmp/a.m4a", durationMs = 0, createdAtMillis = 0),
        )

        assertEquals(FolderDeletionResult.NotEmpty, repository.delete(id))
    }

    @Test
    fun `observeById vrati slozku podle id, po smazani null`() = runTest {
        val id = repository.create("Práce", parentFolderId = null)

        assertEquals("Práce", repository.observeById(id).first()?.name)

        repository.delete(id)

        assertEquals(null, repository.observeById(id).first())
    }
}
