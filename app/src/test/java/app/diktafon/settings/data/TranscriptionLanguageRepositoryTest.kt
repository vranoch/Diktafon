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
class TranscriptionLanguageRepositoryTest {

    private val db = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        AppDatabase::class.java,
    ).allowMainThreadQueries().build()

    private val repository = TranscriptionLanguageRepositoryImpl(db.transcriptionLanguageDao())

    @Test
    fun `pridani a smazani jazyka`() = runTest {
        repository.add("cs", "Čeština")
        repository.add("en", "English")

        assertEquals(listOf("en", "cs"), repository.observeAll().first().map { it.code }.sorted().reversed())

        repository.remove("en")

        assertEquals(listOf("cs"), repository.observeAll().first().map { it.code })
    }

    @Test
    fun `opakovane pridani stejneho kodu prepise nazev`() = runTest {
        repository.add("cs", "Cestina")
        repository.add("cs", "Čeština")

        val languages = repository.observeAll().first()
        assertEquals(1, languages.size)
        assertEquals("Čeština", languages.first().displayName)
    }
}
