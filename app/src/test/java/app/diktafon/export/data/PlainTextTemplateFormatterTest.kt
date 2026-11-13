package app.diktafon.export.data

import app.diktafon.structuring.domain.Template
import app.diktafon.structuring.domain.TemplateField
import org.junit.Assert.assertTrue
import org.junit.Test

class PlainTextTemplateFormatterTest {

    private val formatter = PlainTextTemplateFormatter()

    @Test
    fun `export obsahuje vzorovy text schema instrukce i verzi`() {
        val template = Template(
            id = 1,
            name = "Zápis z porady",
            version = 3,
            fields = listOf(TemplateField("datum", true)),
            instructions = "Vyplň datum",
            exampleText = "Datum: 2026-11-13",
        )

        val text = formatter.format(template)

        assertTrue(text.contains("v3"))
        assertTrue(text.contains("datum"))
        assertTrue(text.contains("Vyplň datum"))
        assertTrue(text.contains("Datum: 2026-11-13"))
    }
}
