package app.diktafon.settings.domain

data class ProviderConfig(
    val sttProviderName: String = "",
    val sttModel: String = "",
    val llmProviderName: String = "",
    val llmModel: String = "",
    val defaultTemplateId: Long? = null,
    val defaultLanguageCode: String = "cs",
    // Vychozi false = systemovy picker pri importu nabidne vsechny soubory, ne jen "audio/*" -
    // nektera uloziste (typicky externi/USB z diktafonu) hlasi spatny/obecny MIME typ, takze by
    // filtr soubory potichu schoval (viz ImportScreen). Format se stejne overuje az po vyberu.
    val importAudioOnlyFilter: Boolean = false,
)
