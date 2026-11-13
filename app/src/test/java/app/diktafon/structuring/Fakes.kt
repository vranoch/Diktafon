package app.diktafon.structuring

import app.diktafon.structuring.domain.DocumentProcessor
import app.diktafon.structuring.domain.StructuredDocument
import app.diktafon.structuring.domain.StructuredDocumentRepository
import app.diktafon.structuring.domain.StructuredDocumentValidator
import app.diktafon.structuring.domain.StructuringRequest
import app.diktafon.structuring.domain.StructuringResult
import app.diktafon.structuring.domain.Template
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

    override suspend fun save(document: StructuredDocument): Long {
        saved += document
        flow.value = saved.toList()
        return saved.size.toLong()
    }

    override fun observeForRecording(recordingId: Long): Flow<List<StructuredDocument>> = flow
}
