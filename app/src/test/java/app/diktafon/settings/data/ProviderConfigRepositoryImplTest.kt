package app.diktafon.settings.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.diktafon.recording.data.AppDatabase
import app.diktafon.settings.domain.ProviderConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ProviderConfigRepositoryImplTest {

    private val db = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        AppDatabase::class.java,
    ).allowMainThreadQueries().build()

    private val repository = ProviderConfigRepositoryImpl(db.providerConfigDao())

    @Test
    fun `bez ulozene konfigurace se vrati vychozi hodnoty vcetne cestiny`() = runTest {
        assertEquals(ProviderConfig(), repository.observe().first())
    }

    @Test
    fun `ulozena konfigurace prepise vychozi hodnoty a dalsi save ji aktualizuje`() = runTest {
        repository.save(ProviderConfig(sttProviderName = "acme", sttModel = "m1", defaultLanguageCode = "en"))
        assertEquals("acme", repository.observe().first().sttProviderName)
        assertEquals("en", repository.observe().first().defaultLanguageCode)

        repository.save(ProviderConfig(sttProviderName = "acme2", sttModel = "m2", defaultLanguageCode = "cs"))

        val updated = repository.observe().first()
        assertEquals("acme2", updated.sttProviderName)
        assertEquals("cs", updated.defaultLanguageCode)
    }
}
