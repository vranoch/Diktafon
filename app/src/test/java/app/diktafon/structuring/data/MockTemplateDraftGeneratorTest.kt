package app.diktafon.structuring.data

import app.diktafon.structuring.domain.TemplateDraftRequest
import app.diktafon.structuring.domain.TemplateDraftResult
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MockTemplateDraftGeneratorTest {

    private val generator = MockTemplateDraftGenerator()

    @Test
    fun `vzorovy text s poli oddelenymi dvojteckou se odvodi na pole sablony`() = runTest {
        val result = generator.generateDraft(
            TemplateDraftRequest(exampleText = "Datum: 2026-11-13\nÚčastníci: Jan, Petr"),
        )

        assertTrue(result is TemplateDraftResult.Success)
        val fieldLabels = (result as TemplateDraftResult.Success).fields.map { it.label }
        assertTrue(fieldLabels.containsAll(listOf("Datum", "Účastníci")))
    }

    @Test
    fun `poznamky k polim rozsiri odvozeny seznam poli`() = runTest {
        val result = generator.generateDraft(
            TemplateDraftRequest(exampleText = "Datum: 2026-11-13", fieldNotes = "Místo, Poznámka"),
        )

        val fieldLabels = (result as TemplateDraftResult.Success).fields.map { it.label }
        assertTrue(fieldLabels.containsAll(listOf("Datum", "Místo", "Poznámka")))
    }

    @Test
    fun `bez rozpoznatelnych poli vrati InvalidOutput`() = runTest {
        val result = generator.generateDraft(TemplateDraftRequest(exampleText = "žádná dvojtečka tady"))

        assertTrue(result is TemplateDraftResult.Failure.InvalidOutput)
    }

    @Test
    fun `klice poli se odvozuji bez diakritiky a jsou v ramci draftu unikatni`() = runTest {
        val result = generator.generateDraft(
            TemplateDraftRequest(exampleText = "Datum narození: 2026-11-13\nDatum (jiné): 2026-11-14"),
        )

        val fields = (result as TemplateDraftResult.Success).fields
        assertEquals("datum_narozeni", fields[0].key)
        assertEquals("datum_jine", fields[1].key)
    }
}
