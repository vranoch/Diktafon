package app.diktafon.structuring.domain

import kotlinx.coroutines.flow.Flow

interface TemplateRepository {
    suspend fun saveNewVersion(
        templateId: Long?,
        name: String,
        fields: List<TemplateField>,
        instructions: String,
        exampleText: String,
        createdAtMillis: Long,
    ): Template

    // Nejnovejsi verze kazde ulozene sablony - "schvaleny katalog" pro vyber v UC-004/UC-010.
    fun observeApprovedCatalog(): Flow<List<Template>>
}
