package app.diktafon.structuring.data

import app.diktafon.structuring.domain.TemplateDraftRequest
import app.diktafon.structuring.domain.TemplateDraftResult
import kotlinx.coroutines.test.runTest
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
        val fieldNames = (result as TemplateDraftResult.Success).fields.map { it.name }
        assertTrue(fieldNames.containsAll(listOf("Datum", "Účastníci")))
    }

    @Test
    fun `poznamky k polim rozsiri odvozeny seznam poli`() = runTest {
        val result = generator.generateDraft(
            TemplateDraftRequest(exampleText = "Datum: 2026-11-13", fieldNotes = "Místo, Poznámka"),
        )

        val fieldNames = (result as TemplateDraftResult.Success).fields.map { it.name }
        assertTrue(fieldNames.containsAll(listOf("Datum", "Místo", "Poznámka")))
    }

    @Test
    fun `bez rozpoznatelnych poli vrati InvalidOutput`() = runTest {
        val result = generator.generateDraft(TemplateDraftRequest(exampleText = "žádná dvojtečka tady"))

        assertTrue(result is TemplateDraftResult.Failure.InvalidOutput)
    }
}
