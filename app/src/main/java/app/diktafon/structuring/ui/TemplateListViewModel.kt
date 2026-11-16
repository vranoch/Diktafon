package app.diktafon.structuring.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.diktafon.structuring.domain.Template
import app.diktafon.structuring.domain.TemplateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TemplateListViewModel @Inject constructor(
    private val templateRepository: TemplateRepository,
) : ViewModel() {

    fun templates(): Flow<List<Template>> = templateRepository.observeApprovedCatalog()

    fun versions(templateId: Long): Flow<List<Template>> = templateRepository.observeVersions(templateId)

    fun delete(templateId: Long) {
        viewModelScope.launch { templateRepository.delete(templateId) }
    }

    fun deleteVersion(templateId: Long, version: Int) {
        viewModelScope.launch { templateRepository.deleteVersion(templateId, version) }
    }
}
