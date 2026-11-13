package app.diktafon.structuring.data

import app.diktafon.structuring.domain.Template
import app.diktafon.structuring.domain.TemplateField
import app.diktafon.structuring.domain.ValidationOutcome
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class JsonSchemaDocumentValidatorTest {

    private val validator = JsonSchemaDocumentValidator()
    private val template = Template(
        id = 1,
        name = "Zápis z porady",
        version = 1,
        fields = listOf(
            TemplateField("datum", required = true),
            TemplateField("ucastnici", required = true),
            TemplateField("poznamka", required = false),
        ),
        instructions = "Vyplň pole podle přepisu.",
    )

    @Test
    fun `platny json se vsemi poli vrati Valid bez chybejicich poli`() {
        val outcome = validator.validate(
            """{"datum":"2026-11-13","ucastnici":"Jan, Petr","poznamka":"OK"}""",
            template,
        )

        assertTrue(outcome is ValidationOutcome.Valid)
        outcome as ValidationOutcome.Valid
        assertEquals("2026-11-13", outcome.fields["datum"])
        assertTrue(outcome.missingRequiredFields.isEmpty())
    }

    @Test
    fun `chybejici povinne pole se oznaci, hodnota se nevymysli`() {
        val outcome = validator.validate(
            """{"datum":"2026-11-13"}""",
            template,
        )

        assertTrue(outcome is ValidationOutcome.Valid)
        outcome as ValidationOutcome.Valid
        assertEquals(listOf("ucastnici"), outcome.missingRequiredFields)
        assertEquals(null, outcome.fields["ucastnici"])
    }

    @Test
    fun `neplatny json vrati InvalidJson`() {
        val outcome = validator.validate("tohle neni json", template)

        assertEquals(ValidationOutcome.InvalidJson, outcome)
    }

    @Test
    fun `json pole misto objektu vrati InvalidJson`() {
        val outcome = validator.validate("""["a", "b"]""", template)

        assertEquals(ValidationOutcome.InvalidJson, outcome)
    }
}
