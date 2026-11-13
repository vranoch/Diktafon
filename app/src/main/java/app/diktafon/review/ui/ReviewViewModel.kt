package app.diktafon.review.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.diktafon.structuring.domain.StructuredDocumentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val repository: StructuredDocumentRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReviewUiState())
    val uiState: StateFlow<ReviewUiState> = _uiState.asStateFlow()

    fun load(documentId: Long, transcriptText: String) {
        _uiState.update { it.copy(documentId = documentId, transcriptText = transcriptText) }
        viewModelScope.launch {
            repository.observeById(documentId).collect { document ->
                document?.let {
                    _uiState.update { state ->
                        state.copy(
                            fields = it.fields,
                            missingRequiredFields = it.missingRequiredFields,
                            isApproved = it.isApproved,
                        )
                    }
                }
            }
        }
    }

    fun onFieldChanged(name: String, newValue: String) {
        _uiState.update { it.copy(fields = it.fields + (name to newValue)) }
    }

    fun saveChanges() {
        val state = _uiState.value
        viewModelScope.launch {
            repository.updateFields(state.documentId, state.fields)
        }
    }

    fun approve() {
        val state = _uiState.value
        viewModelScope.launch {
            repository.updateFields(state.documentId, state.fields)
            repository.approve(state.documentId)
        }
    }
}
