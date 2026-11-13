package app.diktafon.structuring.data

import app.diktafon.structuring.domain.Template
import app.diktafon.structuring.domain.TemplateField
import app.diktafon.structuring.domain.TemplateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import javax.inject.Inject

@Serializable
private data class TemplateFieldDto(val name: String, val required: Boolean)

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
                fieldsJson = Json.encodeToString(fieldsSerializer, fields.map { TemplateFieldDto(it.name, it.required) }),
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
}

private fun TemplateEntity.toDomain() = Template(
    id = templateId,
    name = name,
    version = version,
    fields = Json.decodeFromString(fieldsSerializer, fieldsJson).map { TemplateField(it.name, it.required) },
    instructions = instructions,
    exampleText = exampleText,
)
