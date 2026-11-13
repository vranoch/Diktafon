package app.diktafon.structuring.domain

import kotlinx.coroutines.flow.Flow

interface StructuredDocumentRepository {
    suspend fun save(document: StructuredDocument): Long
    fun observeForRecording(recordingId: Long): Flow<List<StructuredDocument>>
}
