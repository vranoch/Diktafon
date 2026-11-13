package app.diktafon.export.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.diktafon.export.domain.DocumentFormatter
import app.diktafon.settings.domain.EmailRecipientsRepository
import app.diktafon.structuring.domain.StructuredDocument
import app.diktafon.structuring.domain.Template
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExportViewModel @Inject constructor(
    private val formatter: DocumentFormatter,
    private val emailRecipientsRepository: EmailRecipientsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExportUiState())
    val uiState: StateFlow<ExportUiState> = _uiState.asStateFlow()

    fun prepareExport(document: StructuredDocument, template: Template) {
        _uiState.update { it.copy(formattedText = formatter.format(document, template)) }
    }

    fun loadAvailableRecipients() {
        viewModelScope.launch {
            emailRecipientsRepository.observeAll().collect { recipients ->
                _uiState.update { it.copy(availableRecipients = recipients) }
            }
        }
    }

    fun onRecipientToggled(email: String) {
        _uiState.update {
            val current = it.selectedRecipientEmails
            it.copy(selectedRecipientEmails = if (email in current) current - email else current + email)
        }
    }
}
