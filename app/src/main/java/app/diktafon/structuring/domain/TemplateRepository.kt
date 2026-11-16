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

    // Vsechny verze jedne sablony (sestupne), napr. pro vyber verze ke smazani.
    fun observeVersions(templateId: Long): Flow<List<Template>>

    // Prepise obsah existujici verze na miste (zadna nova verze nevznika).
    suspend fun overwriteVersion(
        templateId: Long,
        version: Int,
        name: String,
        fields: List<TemplateField>,
        instructions: String,
        exampleText: String,
    ): Template

    // Smaze vsechny verze dane sablony. Jiz vytvorene strukturovane dokumenty si svuj
    // vlastni snapshot poli nesou nezavisle (viz StructuredDocument.fields), takze smazani
    // sablony je nijak neporusi.
    suspend fun delete(templateId: Long)

    // Smaze jen jednu konkretni verzi; ostatni verze stejne sablony zustanou zachovany.
    suspend fun deleteVersion(templateId: Long, version: Int)
}
