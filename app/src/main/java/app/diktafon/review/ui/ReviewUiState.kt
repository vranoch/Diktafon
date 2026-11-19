package app.diktafon.review.ui

data class ReviewUiState(
    val documentId: Long = 0,
    val recordingId: Long = 0,
    val transcriptText: String = "",
    val fields: Map<String, String?> = emptyMap(),
    // Zobrazovany popisek pro kazdy klic pole (z aktualni verze sablony) - pokud sablona uz
    // neexistuje nebo pole mezitim zmizelo, UI pouzije jako nouzovou variantu primo klic.
    val fieldLabels: Map<String, String> = emptyMap(),
    val missingRequiredFields: List<String> = emptyList(),
    val isApproved: Boolean = false,
    val formattedPreview: String = "",
    val isDiscarded: Boolean = false,
)
