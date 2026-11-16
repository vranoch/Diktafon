package app.diktafon.transcription.ui

enum class TranscriptionPhase { IDLE, TRANSCRIBING, READY, ERROR }

data class TranscriptionUiState(
    val phase: TranscriptionPhase = TranscriptionPhase.IDLE,
    val languageCode: String = "cs",
    val text: String = "",
    // Posledni ulozena hodnota - umoznuje poznat rozpracovanou (neulozenou) editaci a "Zrusit".
    val savedText: String = "",
    val providerName: String? = null,
    val modelName: String? = null,
    val version: Int = 0,
    val errorMessage: String? = null,
)
