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
// nacitaji (doplni se NONE).
@Serializable
private data class TemplateFieldDto(val name: String, val required: Boolean, val autoFill: TemplateFieldAutoFill = TemplateFieldAutoFill.NONE)

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
    Json.encodeToString(fieldsSerializer, fields.map { TemplateFieldDto(it.name, it.required, it.autoFill) })

private fun TemplateEntity.toDomain() = Template(
    id = templateId,
    name = name,
    version = version,
    fields = Json.decodeFromString(fieldsSerializer, fieldsJson).map { TemplateField(it.name, it.required, it.autoFill) },
    instructions = instructions,
    exampleText = exampleText,
)
