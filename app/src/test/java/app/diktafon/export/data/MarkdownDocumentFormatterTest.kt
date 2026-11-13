package app.diktafon.export.data

import app.diktafon.structuring.domain.StructuredDocument
import app.diktafon.structuring.domain.Template
import app.diktafon.structuring.domain.TemplateField
import org.junit.Assert.assertTrue
import org.junit.Test

class MarkdownDocumentFormatterTest {

    private val formatter = MarkdownDocumentFormatter()
    private val template = Template(
        id = 1,
        name = "Zápis z porady",
        version = 1,
        fields = listOf(TemplateField("datum", required = true), TemplateField("ucastnici", required = true)),
        instructions = "",
    )

    @Test
    fun `vyplnene pole se zobrazi jako hodnota`() {
        val document = StructuredDocument(
            recordingId = 1,
            transcriptId = 1,
            templateId = 1,
            templateVersion = 1,
            fields = mapOf("datum" to "2026-11-13", "ucastnici" to "Jan"),
            missingRequiredFields = emptyList(),
            createdAtMillis = 1_000,
        )

        val markdown = formatter.format(document, template)

        assertTrue(markdown.contains("# Zápis z porady"))
        assertTrue(markdown.contains("**datum:** 2026-11-13"))
        assertTrue(markdown.contains("**ucastnici:** Jan"))
    }

    @Test
    fun `chybejici pole se oznaci jako chybejici, ne jako prazdny radek`() {
        val document = StructuredDocument(
            recordingId = 1,
            transcriptId = 1,
            templateId = 1,
            templateVersion = 1,
            fields = mapOf("datum" to "2026-11-13", "ucastnici" to null),
            missingRequiredFields = listOf("ucastnici"),
            createdAtMillis = 1_000,
        )

        val markdown = formatter.format(document, template)

        assertTrue(markdown.contains("**ucastnici:** _(chybí)_"))
    }
}
