package app.diktafon.structuring.data

import app.diktafon.structuring.domain.StructuredDocument
import app.diktafon.structuring.domain.StructuredDocumentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import javax.inject.Inject

private val fieldsSerializer = MapSerializer(String.serializer(), String.serializer().nullable)
private val missingFieldsSerializer = ListSerializer(String.serializer())

class StructuredDocumentRepositoryImpl @Inject constructor(
    private val dao: StructuredDocumentDao,
) : StructuredDocumentRepository {

    override suspend fun save(document: StructuredDocument): Long {
        val entity = StructuredDocumentEntity(
            id = document.id,
            recordingId = document.recordingId,
            transcriptId = document.transcriptId,
            templateId = document.templateId,
            templateVersion = document.templateVersion,
            fieldsJson = Json.encodeToString(fieldsSerializer, document.fields),
            missingFieldsJson = Json.encodeToString(missingFieldsSerializer, document.missingRequiredFields),
            createdAtMillis = document.createdAtMillis,
            isApproved = document.isApproved,
        )
        return dao.insert(entity)
    }

    override suspend fun updateFields(documentId: Long, fields: Map<String, String?>) {
        dao.updateFields(documentId, Json.encodeToString(fieldsSerializer, fields))
    }

    override suspend fun approve(documentId: Long) {
        dao.approve(documentId)
    }

    override fun observeForRecording(recordingId: Long): Flow<List<StructuredDocument>> =
        dao.observeForRecording(recordingId).map { entities -> entities.map { it.toDomain() } }

    override fun observeById(documentId: Long): Flow<StructuredDocument?> =
        dao.observeById(documentId).map { it?.toDomain() }
}

private fun StructuredDocumentEntity.toDomain() = StructuredDocument(
    id = id,
    recordingId = recordingId,
    transcriptId = transcriptId,
    templateId = templateId,
    templateVersion = templateVersion,
    fields = Json.decodeFromString(fieldsSerializer, fieldsJson),
    missingRequiredFields = Json.decodeFromString(missingFieldsSerializer, missingFieldsJson),
    createdAtMillis = createdAtMillis,
    isApproved = isApproved,
)
