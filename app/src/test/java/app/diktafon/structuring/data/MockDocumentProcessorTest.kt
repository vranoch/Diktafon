package app.diktafon.structuring.data

import app.diktafon.structuring.domain.StructuringRequest
import app.diktafon.structuring.domain.StructuringResult
import app.diktafon.structuring.domain.Template
import app.diktafon.structuring.domain.TemplateField
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

class MockDocumentProcessorTest {

    private val processor = MockDocumentProcessor()

    @Test
    fun `vrati uspesny vystup parsovatelny validatorem`() = runTest {
        val template = Template(
            id = 1,
            name = "Test",
            version = 1,
            fields = listOf(TemplateField("a", required = true)),
            instructions = "",
        )

        val result = processor.structure(StructuringRequest("nejaky prepis", template))

        assertTrue(result is StructuringResult.Success)
        val validator = JsonSchemaDocumentValidator()
        val outcome = validator.validate((result as StructuringResult.Success).rawJson, template)
        assertTrue(outcome is app.diktafon.structuring.domain.ValidationOutcome.Valid)
    }
}
