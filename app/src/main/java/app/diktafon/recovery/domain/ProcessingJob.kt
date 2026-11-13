package app.diktafon.recovery.domain

enum class ProcessingJobType { TRANSCRIPTION, STRUCTURING }
enum class ProcessingJobStatus { IN_PROGRESS, DONE, FAILED }

data class ProcessingJob(
    val id: Long = 0,
    val recordingId: Long,
    val type: ProcessingJobType,
    val status: ProcessingJobStatus,
    val updatedAtMillis: Long,
)
