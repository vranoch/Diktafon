package app.diktafon.structuring.data

import app.diktafon.settings.domain.ApiKeyRepository
import app.diktafon.settings.domain.ApiKeySlots
import app.diktafon.settings.domain.ProviderConfigRepository
import app.diktafon.structuring.domain.DocumentProcessor
import app.diktafon.structuring.domain.StructuringRequest
import app.diktafon.structuring.domain.StructuringResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

private const val DEFAULT_MODEL = "gpt-4o-mini"
private const val ENDPOINT = "https://api.openai.com/v1/chat/completions"

class OpenAiDocumentProcessor @Inject constructor(
    private val apiKeyRepository: ApiKeyRepository,
    private val providerConfigRepository: ProviderConfigRepository,
    private val httpClient: OkHttpClient,
) : DocumentProcessor {

    override suspend fun structure(request: StructuringRequest): StructuringResult {
        val apiKey = apiKeyRepository.getApiKey(ApiKeySlots.LLM)
            ?: return StructuringResult.Failure.Unauthorized
        val model = providerConfigRepository.observe().first().llmModel.ifBlank { DEFAULT_MODEL }

        val payload = buildJsonObject {
            put("model", model)
            put("response_format", buildJsonObject { put("type", "json_object") })
            putJsonArray("messages") {
                add(buildJsonObject {
                    put("role", "system")
                    put("content", buildSystemPrompt(request))
                })
                add(buildJsonObject {
                    put("role", "user")
                    put("content", request.transcriptText)
                })
            }
        }

        return withContext(Dispatchers.IO) {
            try {
                val httpRequest = Request.Builder()
                    .url(ENDPOINT)
                    .header("Authorization", "Bearer $apiKey")
                    .post(payload.toString().toRequestBody("application/json".toMediaType()))
                    .build()

                httpClient.newCall(httpRequest).execute().use { response ->
                    mapResponse(response.code, response.body?.string().orEmpty())
                }
            } catch (e: SocketTimeoutException) {
                StructuringResult.Failure.Timeout
            } catch (e: IOException) {
                StructuringResult.Failure.ServerError(e)
            }
        }
    }

    private fun buildSystemPrompt(request: StructuringRequest): String = buildString {
        val fieldList = request.template.fields.joinToString(", ") {
            "\"${it.key}\" (popisek: \"${it.label}\")" + if (it.required) " (povinné)" else ""
        }
        append("Jsi asistent, ktery z prepisu hlasove nahravky vytvari strukturovany dokument. ")
        append("Vrat VYHRADNE JSON objekt s presne temito klici: $fieldList. ")
        append("Pokud hodnotu v prepisu nenajdes, nastav ji na null - nikdy si hodnotu nedomyslej. ")
        if (request.template.instructions.isNotBlank()) {
            append("Instrukce k sablone: ${request.template.instructions}. ")
        }
        if (request.template.exampleText.isNotBlank()) {
            append("Priklad ocekavaneho stylu vystupu: ${request.template.exampleText}.")
        }
    }

    internal fun mapResponse(httpCode: Int, responseBody: String): StructuringResult = when (httpCode) {
        200 -> {
            val content = runCatching {
                val root = Json.parseToJsonElement(responseBody).jsonObject
                val choices = root["choices"] as? JsonArray
                choices?.firstOrNull()?.jsonObject?.get("message")?.jsonObject?.get("content")?.jsonPrimitive?.contentOrNull
            }.getOrNull()
            if (content.isNullOrBlank()) {
                StructuringResult.Failure.RejectedByModel
            } else {
                StructuringResult.Success(rawJson = content)
            }
        }
        401, 403 -> StructuringResult.Failure.Unauthorized
        429 -> StructuringResult.Failure.ServerError()
        in 500..599 -> StructuringResult.Failure.ServerError()
        else -> StructuringResult.Failure.RejectedByModel
    }
}
