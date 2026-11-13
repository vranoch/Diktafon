package app.diktafon.export.domain

import app.diktafon.structuring.domain.StructuredDocument
import app.diktafon.structuring.domain.Template

interface DocumentFormatter {
    fun format(document: StructuredDocument, template: Template): String
}
