package app.diktafon.structuring.ui

enum class StructuringPhase { IDLE, PROCESSING, READY, ERROR }

data class StructuringUiState(
    val phase: StructuringPhase = StructuringPhase.IDLE,
    val fields: Map<String, String?> = emptyMap(),
    val missingRequiredFields: List<String> = emptyList(),
    val savedDocumentId: Long? = null,
    val errorMessage: String? = null,
)
