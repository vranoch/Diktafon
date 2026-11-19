package app.diktafon.structuring.data

import app.diktafon.settings.domain.ApiKeyRepository
import app.diktafon.settings.domain.ApiKeySlots
import app.diktafon.settings.domain.ProviderConfigRepository
import app.diktafon.structuring.domain.Template
import app.diktafon.structuring.domain.TemplateNameRecognizer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.longOrNull
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

private const val SYSTEM_PROMPT = "Jsi asistent, ktery z prepisu hlasove nahravky pozna, kterou " +
    "z nabidnutych znamych sablon chtel uzivatel pouzit - napr. rekl jeji nazev nebo na ni jasne " +
    "odkazal. Vyber VYHRADNE jedno z nabidnutych id ze seznamu, nikdy nevymyslej novou sablonu " +
    "ani jeji nazev. Pokud zadna sablona jasne neodpovida, vrat null. Vrat VYHRADNE JSON objekt " +
    "{\"templateId\": <cislo nebo null>}."

/**
 * Rozpoznavani je jen pomocny navrh (FR-AUD-010) - jakekoliv selhani (chybejici klic, sitova
 * chyba, nerozpoznatelna odpoved) se tiše resi jako "nerozpoznano", uzivatel si pak sablonu
 * zvoli rucne. Nejde o kritickou operaci jako prepis/strukturovani, kde se chyba hlasi.
 */
class OpenAiTemplateNameRecognizer @Inject constructor(
    private val apiKeyRepository: ApiKeyRepository,
    private val providerConfigRepository: ProviderConfigRepository,
    private val httpClient: OkHttpClient,
) : TemplateNameRecognizer {

    override suspend fun recognize(transcriptText: String, approvedCatalog: List<Template>): Template? {
        if (approvedCatalog.isEmpty()) return null
        val apiKey = apiKeyRepository.getApiKey(ApiKeySlots.LLM) ?: return null
        val model = providerConfigRepository.observe().first().llmModel.ifBlank { DEFAULT_MODEL }

        val catalogList = approvedCatalog.joinToString("\n") { "id ${it.id}: ${it.name}" }
        val userContent = "Přepis:\n$transcriptText\n\nZnámé šablony:\n$catalogList"

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
                    mapResponse(response.code, response.body?.string().orEmpty(), approvedCatalog)
                }
            } catch (e: SocketTimeoutException) {
                null
            } catch (e: IOException) {
                null
            }
        }
    }

    internal fun mapResponse(httpCode: Int, responseBody: String, approvedCatalog: List<Template>): Template? {
        if (httpCode != 200) return null
        return runCatching {
            val root = Json.parseToJsonElement(responseBody).jsonObject
            val choices = root["choices"] as? JsonArray
            val content = choices?.firstOrNull()?.jsonObject?.get("message")?.jsonObject?.get("content")
                ?.jsonPrimitive?.contentOrNull ?: return@runCatching null
            val draft = Json.parseToJsonElement(content).jsonObject
            val templateId = draft["templateId"]?.jsonPrimitive?.longOrNull ?: return@runCatching null
            approvedCatalog.firstOrNull { it.id == templateId }
        }.getOrNull()
    }
}
