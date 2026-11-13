package app.diktafon.structuring.domain

import kotlinx.coroutines.flow.Flow

interface StructuredDocumentRepository {
    suspend fun save(document: StructuredDocument): Long
    suspend fun updateFields(documentId: Long, fields: Map<String, String?>)
    suspend fun approve(documentId: Long)
    fun observeForRecording(recordingId: Long): Flow<List<StructuredDocument>>
    fun observeById(documentId: Long): Flow<StructuredDocument?>
}
