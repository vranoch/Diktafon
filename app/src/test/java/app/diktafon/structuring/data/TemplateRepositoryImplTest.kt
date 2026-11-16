package app.diktafon.structuring.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.diktafon.recording.data.AppDatabase
import app.diktafon.structuring.domain.TemplateField
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TemplateRepositoryImplTest {

    private val db = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        AppDatabase::class.java,
    ).allowMainThreadQueries().build()

    private val repository = TemplateRepositoryImpl(db.templateDao())

    @Test
    fun `nova sablona dostane verzi 1`() = runTest {
        val saved = repository.saveNewVersion(
            templateId = null,
            name = "Zápis z porady",
            fields = listOf(TemplateField("datum", true)),
            instructions = "instrukce",
            exampleText = "vzor",
            createdAtMillis = 1_000,
        )

        assertEquals(1, saved.version)
        assertEquals(listOf("Zápis z porady"), repository.observeApprovedCatalog().first().map { it.name })
    }

    @Test
    fun `dalsi verze stejne sablony se zvysi a katalog obsahuje jen nejnovejsi`() = runTest {
        val v1 = repository.saveNewVersion(null, "Zápis", listOf(TemplateField("a", true)), "i1", "e1", 1_000)
        val v2 = repository.saveNewVersion(v1.id, "Zápis", listOf(TemplateField("a", true), TemplateField("b", false)), "i2", "e2", 2_000)

        assertEquals(2, v2.version)
        val catalog = repository.observeApprovedCatalog().first()
        assertEquals(1, catalog.size)
        assertEquals(2, catalog[0].version)
        assertEquals(2, catalog[0].fields.size)
    }

    @Test
    fun `dve ruzne sablony se v katalogu objevi obe`() = runTest {
        repository.saveNewVersion(null, "Zápis", listOf(TemplateField("a", true)), "i", "e", 1_000)
        repository.saveNewVersion(null, "Lékařská zpráva", listOf(TemplateField("b", true)), "i", "e", 1_000)

        val catalog = repository.observeApprovedCatalog().first()
        assertEquals(2, catalog.size)
    }

    @Test
    fun `smazani sablony odstrani vsechny jeji verze, ostatni sablony zustanou`() = runTest {
        val v1 = repository.saveNewVersion(null, "Zápis", listOf(TemplateField("a", true)), "i1", "e1", 1_000)
        repository.saveNewVersion(v1.id, "Zápis", listOf(TemplateField("a", true)), "i2", "e2", 2_000)
        repository.saveNewVersion(null, "Lékařská zpráva", listOf(TemplateField("b", true)), "i", "e", 1_000)

        repository.delete(v1.id)

        val catalog = repository.observeApprovedCatalog().first()
        assertEquals(listOf("Lékařská zpráva"), catalog.map { it.name })
    }

    @Test
    fun `smazani jedne verze zachova ostatni verze stejne sablony`() = runTest {
        val v1 = repository.saveNewVersion(null, "Zápis", listOf(TemplateField("a", true)), "i1", "e1", 1_000)
        repository.saveNewVersion(v1.id, "Zápis", listOf(TemplateField("a", true)), "i2", "e2", 2_000)
        repository.saveNewVersion(v1.id, "Zápis", listOf(TemplateField("a", true)), "i3", "e3", 3_000)

        repository.deleteVersion(v1.id, 2)

        val versions = repository.observeVersions(v1.id).first()
        assertEquals(listOf(3, 1), versions.map { it.version })
    }

    @Test
    fun `prepsani verze zmeni obsah bez vzniku nove verze`() = runTest {
        val v1 = repository.saveNewVersion(null, "Zápis", listOf(TemplateField("a", true)), "i1", "e1", 1_000)

        val overwritten = repository.overwriteVersion(
            templateId = v1.id,
            version = v1.version,
            name = "Zápis z porady",
            fields = listOf(TemplateField("a", true), TemplateField("b", false)),
            instructions = "nove instrukce",
            exampleText = "novy vzor",
        )

        assertEquals(1, overwritten.version)
        val versions = repository.observeVersions(v1.id).first()
        assertEquals(1, versions.size)
        assertEquals("Zápis z porady", versions[0].name)
        assertEquals(2, versions[0].fields.size)
    }
}
