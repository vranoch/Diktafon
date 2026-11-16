package app.diktafon.structuring.ui

import androidx.lifecycle.ViewModel
import app.diktafon.structuring.domain.Template
import app.diktafon.structuring.domain.TemplateNameRecognizer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class TemplateSuggestionViewModel @Inject constructor(
    private val recognizer: TemplateNameRecognizer,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TemplateSuggestionUiState())
    val uiState: StateFlow<TemplateSuggestionUiState> = _uiState.asStateFlow()

    fun recognizeFromTranscript(transcriptText: String, approvedCatalog: List<Template>) {
        val suggestion = recognizer.recognize(transcriptText, approvedCatalog)
        _uiState.update { current ->
            current.copy(
                approvedCatalog = approvedCatalog,
                suggestedTemplate = suggestion,
                // Pokud uzivatel uz vybral rucne, opakovane rozpoznani (napr. po doruceni
                // katalogu, ktery pri prvnim vstupu jeste nebyl nacteny) jeho volbu neprepise.
                selectedTemplate = if (current.userChangedSelection) current.selectedTemplate else suggestion,
                wasRecognized = suggestion != null,
            )
        }
    }

    fun selectTemplate(template: Template) {
        _uiState.update { it.copy(selectedTemplate = template, userChangedSelection = true) }
    }
}
