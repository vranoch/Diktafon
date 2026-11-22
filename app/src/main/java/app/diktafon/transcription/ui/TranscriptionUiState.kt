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
    // Cerstvy vysledek STT jeste neni ulozeny jako nova revize - ceka na vyjadreni uzivatele
    // (acceptTranscription/rejectTranscription), ne na porovnani s savedText jako u rucni editace.
    val isPendingAccept: Boolean = false,
    // Signal pro obrazovku, aby se vratila na predchozi obrazovku - nastavuje se jak po
    // prijmuti, tak po odmitnuti (v obou pripadech se ma pokracovat zpet, viz FR/UC k prepisu).
    val isFinished: Boolean = false,
)
