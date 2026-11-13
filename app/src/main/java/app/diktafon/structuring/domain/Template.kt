package app.diktafon.structuring.domain

data class TemplateField(val name: String, val required: Boolean)

data class Template(
    val id: Long,
    val name: String,
    val version: Int,
    val fields: List<TemplateField>,
    val instructions: String,
)
