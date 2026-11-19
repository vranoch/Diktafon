package app.diktafon.structuring.data

import app.diktafon.settings.domain.ApiKeyRepository
import app.diktafon.settings.domain.ApiKeySlots
import app.diktafon.settings.domain.ProviderConfigRepository
import app.diktafon.structuring.domain.TemplateDraftGenerator
import app.diktafon.structuring.domain.TemplateDraftRequest
import app.diktafon.structuring.domain.TemplateDraftResult
import app.diktafon.structuring.domain.TemplateField
import app.diktafon.structuring.domain.generateFieldKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.boolean
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

private const val SYSTEM_PROMPT = "Jsi asistent, ktery z ukazkoveho textu vysledneho dokumentu " +
    "odvozuje strukturu sablony pro jeho opakovane vytvareni z budoucich prepisu. Vrat VYHRADNE " +
    "JSON objekt s klici \"fields\" (pole objektu {\"name\": string, \"required\": boolean}) a " +
    "\"instructions\" (textovy popis pro model, jak dane pole z prepisu vyplnit). Pole rozpoznej " +
    "podle zjevne strukturovanych casti vzoru (napr. nadpisy/zkratky oddelene dvojteckou, " +
    "opakovane sekce) - nazvy poli odvod jako vystizne, cloveku srozumitelne popisky, ne doslovne " +
    "useknuty text pred dvojteckou. Pokud uzivatel dodal poznamky k polim, zohledni je."

class OpenAiTemplateDraftGenerator @Inject constructor(
    private val apiKeyRepository: ApiKeyRepository,
    private val providerConfigRepository: ProviderConfigRepository,
    private val httpClient: OkHttpClient,
) : TemplateDraftGenerator {

    override suspend fun generateDraft(request: TemplateDraftRequest): TemplateDraftResult {
        val apiKey = apiKeyRepository.getApiKey(ApiKeySlots.LLM)
            ?: return TemplateDraftResult.Failure.Unauthorized("V Administraci chybí LLM API klíč.")
        val model = providerConfigRepository.observe().first().llmModel.ifBlank { DEFAULT_MODEL }

        val userContent = buildString {
            append("Vzorový text výsledného dokumentu:\n")
            append(request.exampleText)
            if (request.fieldNotes.isNotBlank()) {
                append("\n\nPoznámky k polím: ")
                append(request.fieldNotes)
            }
        }

        val payload = buildJsonObject {
            put("model", model)
            put("response_format", buildJsonObject { put("type", "json_object") })
            putJsonArray("messages") {
                add(buildJsonObject { put("role", "system"); put("content", SYSTEM_PROMPT) })
                add(buildJsonObject { put("role", "user"); put("content", userContent) })
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
                TemplateDraftResult.Failure.Timeout
            } catch (e: IOException) {
                TemplateDraftResult.Failure.ServerError(e)
            }
        }
    }

    internal fun mapResponse(httpCode: Int, responseBody: String): TemplateDraftResult = when (httpCode) {
        200 -> runCatching {
            val root = Json.parseToJsonElement(responseBody).jsonObject
            val choices = root["choices"] as? JsonArray
            val content = choices?.firstOrNull()?.jsonObject?.get("message")?.jsonObject?.get("content")
                ?.jsonPrimitive?.contentOrNull
                ?: return TemplateDraftResult.Failure.InvalidOutput

            val draft = Json.parseToJsonElement(content).jsonObject
            val fields = mutableListOf<TemplateField>()
            for (element in (draft["fields"] as? JsonArray).orEmpty()) {
                val obj = element.jsonObject
                val label = obj["name"]?.jsonPrimitive?.contentOrNull ?: continue
                val required = runCatching { obj["required"]?.jsonPrimitive?.boolean }.getOrNull() ?: true
                fields += TemplateField(key = generateFieldKey(label, fields.map { it.key }), label = label, required = required)
            }
            val instructions = draft["instructions"]?.jsonPrimitive?.contentOrNull.orEmpty()

            if (fields.isEmpty()) {
                TemplateDraftResult.Failure.InvalidOutput
            } else {
                TemplateDraftResult.Success(fields = fields, instructions = instructions)
            }
        }.getOrElse { TemplateDraftResult.Failure.InvalidOutput }
        401, 403 -> TemplateDraftResult.Failure.Unauthorized()
        429 -> TemplateDraftResult.Failure.ServerError()
        in 500..599 -> TemplateDraftResult.Failure.ServerError()
        else -> TemplateDraftResult.Failure.RejectedByModel
    }
}
