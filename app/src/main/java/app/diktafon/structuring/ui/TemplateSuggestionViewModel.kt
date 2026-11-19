package app.diktafon.structuring.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.diktafon.structuring.domain.Template
import app.diktafon.structuring.domain.TemplateNameRecognizer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TemplateSuggestionViewModel @Inject constructor(
    private val recognizer: TemplateNameRecognizer,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TemplateSuggestionUiState())
    val uiState: StateFlow<TemplateSuggestionUiState> = _uiState.asStateFlow()

    fun recognizeFromTranscript(transcriptText: String, approvedCatalog: List<Template>) {
        _uiState.update { it.copy(approvedCatalog = approvedCatalog, isRecognizing = true) }
        viewModelScope.launch {
            val suggestion = recognizer.recognize(transcriptText, approvedCatalog)
            _uiState.update { current ->
                current.copy(
                    suggestedTemplate = suggestion,
                    // Pokud uzivatel uz vybral rucne, opakovane rozpoznani (napr. po doruceni
                    // katalogu, ktery pri prvnim vstupu jeste nebyl nacteny) jeho volbu neprepise.
                    selectedTemplate = if (current.userChangedSelection) current.selectedTemplate else suggestion,
                    wasRecognized = suggestion != null,
                    isRecognizing = false,
                )
            }
        }
    }

    fun selectTemplate(template: Template) {
        _uiState.update { it.copy(selectedTemplate = template, userChangedSelection = true) }
    }
}
