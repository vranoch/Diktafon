package app.diktafon.structuring.data

import app.diktafon.structuring.domain.StructuredDocumentValidator
import app.diktafon.structuring.domain.Template
import app.diktafon.structuring.domain.ValidationOutcome
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import javax.inject.Inject

class JsonSchemaDocumentValidator @Inject constructor() : StructuredDocumentValidator {

    override fun validate(rawJson: String, template: Template): ValidationOutcome {
        val jsonObject = try {
            Json.parseToJsonElement(rawJson) as? JsonObject ?: return ValidationOutcome.InvalidJson
        } catch (e: SerializationException) {
            return ValidationOutcome.InvalidJson
        }

        val fields = mutableMapOf<String, String?>()
        val missing = mutableListOf<String>()
        for (field in template.fields) {
            val element = jsonObject[field.name]
            val text = when {
                element == null || element is JsonNull -> null
                element is JsonPrimitive -> element.contentOrNull
                else -> element.toString()
            }
            fields[field.name] = text
            if (field.required && text.isNullOrBlank()) {
                missing += field.name
            }
        }

        return ValidationOutcome.Valid(fields = fields, missingRequiredFields = missing)
    }
}
