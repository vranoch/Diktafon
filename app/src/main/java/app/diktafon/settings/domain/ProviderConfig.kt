package app.diktafon.settings.domain

data class ProviderConfig(
    val sttProviderName: String = "",
    val sttModel: String = "",
    val llmProviderName: String = "",
    val llmModel: String = "",
    val defaultTemplateId: Long? = null,
    val defaultLanguageCode: String = "cs",
)
