package app.diktafon.deletion.domain

sealed interface DeletionResult {
    data object Success : DeletionResult
    data class PartialFailure(val failedFilePaths: List<String>) : DeletionResult
}

interface DataDeletionRepository {
    suspend fun deleteRecordingWithAllArtifacts(recordingId: Long): DeletionResult
    suspend fun deleteTranscriptsForRecording(recordingId: Long): DeletionResult
    suspend fun deleteStructuredDocumentsForRecording(recordingId: Long): DeletionResult
}
