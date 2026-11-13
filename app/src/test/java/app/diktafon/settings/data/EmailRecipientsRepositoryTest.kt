package app.diktafon.settings.data

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
class EmailRecipientsRepositoryTest {

    private val db = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        AppDatabase::class.java,
    ).allowMainThreadQueries().build()

    private val repository = EmailRecipientsRepositoryImpl(db.emailRecipientDao())

    @Test
    fun `pridani upraveni a smazani adresy`() = runTest {
        val id = repository.add("jan@example.com")
        assertEquals(listOf("jan@example.com"), repository.observeAll().first().map { it.email })

        repository.update(id, "jan.novak@example.com")
        assertEquals(listOf("jan.novak@example.com"), repository.observeAll().first().map { it.email })

        repository.remove(id)
        assertEquals(emptyList<String>(), repository.observeAll().first().map { it.email })
    }
}
