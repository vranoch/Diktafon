package app.diktafon.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.diktafon.settings.domain.EmailRecipient
import app.diktafon.settings.domain.EmailRecipientsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmailRecipientsViewModel @Inject constructor(
    private val repository: EmailRecipientsRepository,
) : ViewModel() {

    fun recipients(): Flow<List<EmailRecipient>> = repository.observeAll()

    fun add(email: String) {
        if (email.isBlank()) return
        viewModelScope.launch { repository.add(email) }
    }

    fun remove(id: Long) {
        viewModelScope.launch { repository.remove(id) }
    }
}
