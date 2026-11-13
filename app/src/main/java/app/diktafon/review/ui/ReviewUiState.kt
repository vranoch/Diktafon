package app.diktafon.review.ui

data class ReviewUiState(
    val documentId: Long = 0,
    val transcriptText: String = "",
    val fields: Map<String, String?> = emptyMap(),
    val missingRequiredFields: List<String> = emptyList(),
    val isApproved: Boolean = false,
)
