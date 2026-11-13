package app.diktafon.structuring.ui

import androidx.lifecycle.ViewModel
import app.diktafon.structuring.domain.Template
import app.diktafon.structuring.domain.TemplateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class TemplateListViewModel @Inject constructor(
    private val templateRepository: TemplateRepository,
) : ViewModel() {

    fun templates(): Flow<List<Template>> = templateRepository.observeApprovedCatalog()
}
