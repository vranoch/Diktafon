package app.diktafon.structuring

import app.diktafon.structuring.domain.DocumentProcessor
import app.diktafon.structuring.domain.StructuredDocument
import app.diktafon.structuring.domain.StructuredDocumentRepository
import app.diktafon.structuring.domain.StructuredDocumentValidator
import app.diktafon.structuring.domain.StructuringRequest
import app.diktafon.structuring.domain.StructuringResult
import app.diktafon.structuring.domain.Template
import app.diktafon.structuring.domain.TemplateDraftGenerator
import app.diktafon.structuring.domain.TemplateDraftRequest
import app.diktafon.structuring.domain.TemplateDraftResult
import app.diktafon.structuring.domain.TemplateField
import app.diktafon.structuring.domain.TemplateRepository
import app.diktafon.structuring.domain.ValidationOutcome
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeDocumentProcessor(private val result: StructuringResult) : DocumentProcessor {
    override suspend fun structure(request: StructuringRequest): StructuringResult = result
}

class FakeStructuredDocumentValidator(private val outcome: ValidationOutcome) : StructuredDocumentValidator {
    override fun validate(rawJson: String, template: Template): ValidationOutcome = outcome
}

class FakeStructuredDocumentRepository : StructuredDocumentRepository {
    val saved = mutableListOf<StructuredDocument>()
    private val flow = MutableStateFlow<List<StructuredDocument>>(emptyList())
    private val byId = mutableMapOf<Long, MutableStateFlow<StructuredDocument?>>()

    override suspend fun save(document: StructuredDocument): Long {
        val id = saved.size.toLong() + 1
        val withId = document.copy(id = id)
        saved += withId
        flow.value = saved.toList()
        byId.getOrPut(id) { MutableStateFlow(null) }.value = withId
        return id
    }

    override suspend fun updateFields(documentId: Long, fields: Map<String, String?>) {
        val index = saved.indexOfFirst { it.id == documentId }
        if (index == -1) return
        val updated = saved[index].copy(fields = fields)
        saved[index] = updated
        flow.value = saved.toList()
        byId.getOrPut(documentId) { MutableStateFlow(null) }.value = updated
    }

    override suspend fun approve(documentId: Long) {
        val index = saved.indexOfFirst { it.id == documentId }
        if (index == -1) return
        val updated = saved[index].copy(isApproved = true)
        saved[index] = updated
        flow.value = saved.toList()
        byId.getOrPut(documentId) { MutableStateFlow(null) }.value = updated
    }

    override fun observeForRecording(recordingId: Long): Flow<List<StructuredDocument>> = flow

    override fun observeById(documentId: Long): Flow<StructuredDocument?> =
        byId.getOrPut(documentId) { MutableStateFlow(null) }
}

class FakeTemplateDraftGenerator(private val result: TemplateDraftResult) : TemplateDraftGenerator {
    override suspend fun generateDraft(request: TemplateDraftRequest): TemplateDraftResult = result
}

class FakeTemplateRepository : TemplateRepository {
    val saved = mutableListOf<Template>()
    private val flow = MutableStateFlow<List<Template>>(emptyList())
    private val versionFlows = mutableMapOf<Long, MutableStateFlow<List<Template>>>()

    override suspend fun saveNewVersion(
        templateId: Long?,
        name: String,
        fields: List<TemplateField>,
        instructions: String,
        exampleText: String,
        createdAtMillis: Long,
    ): Template {
        val resolvedId = templateId ?: (saved.maxOfOrNull { it.id } ?: 0) + 1
        val nextVersion = (saved.filter { it.id == resolvedId }.maxOfOrNull { it.version } ?: 0) + 1
        val template = Template(resolvedId, name, nextVersion, fields, instructions, exampleText)
        saved += template
        refresh()
        return template
    }

    override fun observeApprovedCatalog(): Flow<List<Template>> = flow

    override fun observeVersions(templateId: Long): Flow<List<Template>> =
        versionFlows.getOrPut(templateId) { MutableStateFlow(versionsOf(templateId)) }

    override suspend fun overwriteVersion(
        templateId: Long,
        version: Int,
        name: String,
        fields: List<TemplateField>,
        instructions: String,
        exampleText: String,
    ): Template {
        val index = saved.indexOfFirst { it.id == templateId && it.version == version }
        val template = Template(templateId, name, version, fields, instructions, exampleText)
        if (index == -1) saved += template else saved[index] = template
        refresh()
        return template
    }

    override suspend fun delete(templateId: Long) {
        saved.removeAll { it.id == templateId }
        refresh()
    }

    override suspend fun deleteVersion(templateId: Long, version: Int) {
        saved.removeAll { it.id == templateId && it.version == version }
        refresh()
    }

    private fun versionsOf(templateId: Long): List<Template> =
        saved.filter { it.id == templateId }.sortedByDescending { it.version }

    private fun refresh() {
        flow.value = saved.groupBy { it.id }.values.map { versions -> versions.maxBy { it.version } }
        versionFlows.forEach { (templateId, stateFlow) -> stateFlow.value = versionsOf(templateId) }
    }
}
