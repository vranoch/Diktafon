package app.diktafon.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.diktafon.settings.domain.TranscriptionLanguage
import app.diktafon.settings.domain.TranscriptionLanguageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LanguagesViewModel @Inject constructor(
    private val repository: TranscriptionLanguageRepository,
) : ViewModel() {

    fun languages(): Flow<List<TranscriptionLanguage>> = repository.observeAll()

    fun add(code: String, displayName: String) {
        if (code.isBlank() || displayName.isBlank()) return
        viewModelScope.launch { repository.add(code.trim(), displayName.trim()) }
    }

    fun remove(code: String) {
        viewModelScope.launch { repository.remove(code) }
    }
}
