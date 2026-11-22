package app.diktafon.settings.ui

data class ApiConfigUiState(
    val sttProviderName: String = "",
    val sttModel: String = "",
    val llmProviderName: String = "",
    val llmModel: String = "",
    val defaultLanguageCode: String = "cs",
    val importAudioOnlyFilter: Boolean = false,
    val hasSttApiKey: Boolean = false,
    val hasLlmApiKey: Boolean = false,
    val statusMessage: String? = null,
    val errorMessage: String? = null,
)
