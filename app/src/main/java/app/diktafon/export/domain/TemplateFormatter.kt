package app.diktafon.export.domain

import app.diktafon.structuring.domain.Template

interface TemplateFormatter {
    fun format(template: Template): String
}
