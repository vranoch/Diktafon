package app.diktafon.structuring.data

import app.diktafon.structuring.domain.Template
import app.diktafon.structuring.domain.TemplateField
import app.diktafon.structuring.domain.TemplateFieldAutoFill
import app.diktafon.structuring.domain.TemplateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import javax.inject.Inject

// autoFill ma vychozi hodnotu, takze starsi ulozena data bez tohoto klice se porad korektne
// nacitaji (doplni se NONE). "name" je zachovane jen kvuli zpetne kompatibilite s daty
// ulozenymi pred rozdelenim pole na key/label (viz ADR-004, aktualizace 2026-11-19) - u tech se
// pri nacteni pouzije jako label i jako key beze zmeny, aby zustaly funkcni uz existujici
// strukturovane dokumenty odkazujici na puvodni (neslugifikovany) klic.
@Serializable
private data class TemplateFieldDto(
    val key: String = "",
    val label: String = "",
    val name: String = "",
    val required: Boolean,
    val autoFill: TemplateFieldAutoFill = TemplateFieldAutoFill.NONE,
)

private val fieldsSerializer = ListSerializer(TemplateFieldDto.serializer())

class TemplateRepositoryImpl @Inject constructor(
    private val dao: TemplateDao,
) : TemplateRepository {

    override suspend fun saveNewVersion(
        templateId: Long?,
        name: String,
        fields: List<TemplateField>,
        instructions: String,
        exampleText: String,
        createdAtMillis: Long,
    ): Template {
        val resolvedTemplateId = templateId ?: dao.nextTemplateId()
        val nextVersion = dao.maxVersion(resolvedTemplateId) + 1
        dao.insert(
            TemplateEntity(
                templateId = resolvedTemplateId,
                name = name,
                version = nextVersion,
                fieldsJson = encodeFields(fields),
                instructions = instructions,
                exampleText = exampleText,
                createdAtMillis = createdAtMillis,
            ),
        )
        return Template(
            id = resolvedTemplateId,
            name = name,
            version = nextVersion,
            fields = fields,
            instructions = instructions,
            exampleText = exampleText,
        )
    }

    override fun observeApprovedCatalog(): Flow<List<Template>> =
        dao.observeApprovedCatalog().map { entities -> entities.map { it.toDomain() } }

    override fun observeVersions(templateId: Long): Flow<List<Template>> =
        dao.observeVersions(templateId).map { entities -> entities.map { it.toDomain() } }

    override suspend fun overwriteVersion(
        templateId: Long,
        version: Int,
        name: String,
        fields: List<TemplateField>,
        instructions: String,
        exampleText: String,
    ): Template {
        dao.updateVersion(templateId, version, name, encodeFields(fields), instructions, exampleText)
        return Template(
            id = templateId,
            name = name,
            version = version,
            fields = fields,
            instructions = instructions,
            exampleText = exampleText,
        )
    }

    override suspend fun delete(templateId: Long) = dao.deleteAllVersions(templateId)

    override suspend fun deleteVersion(templateId: Long, version: Int) = dao.deleteVersion(templateId, version)
}

private fun encodeFields(fields: List<TemplateField>): String =
    Json.encodeToString(
        fieldsSerializer,
        fields.map { TemplateFieldDto(key = it.key, label = it.label, required = it.required, autoFill = it.autoFill) },
    )

private fun TemplateEntity.toDomain() = Template(
    id = templateId,
    name = name,
    version = version,
    fields = Json.decodeFromString(fieldsSerializer, fieldsJson).map { dto ->
        val label = dto.label.ifBlank { dto.name }
        TemplateField(key = dto.key.ifBlank { dto.name }, label = label, required = dto.required, autoFill = dto.autoFill)
    },
    instructions = instructions,
    exampleText = exampleText,
)
