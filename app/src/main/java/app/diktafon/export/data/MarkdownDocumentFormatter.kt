package app.diktafon.export.data

import app.diktafon.export.domain.DocumentFormatter
import app.diktafon.structuring.domain.StructuredDocument
import app.diktafon.structuring.domain.Template
import javax.inject.Inject

class MarkdownDocumentFormatter @Inject constructor() : DocumentFormatter {

    override fun format(document: StructuredDocument, template: Template): String = buildString {
        appendLine("# ${template.name}")
        appendLine()
        for (field in template.fields) {
            val value = document.fields[field.name]
            if (value.isNullOrBlank()) {
                appendLine("**${field.name}:** _(chybí)_")
            } else {
                appendLine("**${field.name}:** $value")
            }
        }
    }
}
