package app.diktafon.export.ui

import app.diktafon.settings.domain.EmailRecipient

data class ExportUiState(
    val formattedText: String = "",
    val availableRecipients: List<EmailRecipient> = emptyList(),
    val selectedRecipientEmails: Set<String> = emptySet(),
)
