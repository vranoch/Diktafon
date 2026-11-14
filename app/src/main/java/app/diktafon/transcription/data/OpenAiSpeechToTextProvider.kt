package app.diktafon.transcription.data

import app.diktafon.settings.domain.ApiKeyRepository
import app.diktafon.settings.domain.ApiKeySlots
import app.diktafon.settings.domain.ProviderConfigRepository
import app.diktafon.transcription.domain.SpeechToTextProvider
import app.diktafon.transcription.domain.TranscriptionRequest
import app.diktafon.transcription.domain.TranscriptionResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

private const val DEFAULT_MODEL = "whisper-1"
private const val ENDPOINT = "https://api.openai.com/v1/audio/transcriptions"

class OpenAiSpeechToTextProvider @Inject constructor(
    private val apiKeyRepository: ApiKeyRepository,
    private val providerConfigRepository: ProviderConfigRepository,
    private val httpClient: OkHttpClient,
) : SpeechToTextProvider {

    override suspend fun transcribe(request: TranscriptionRequest): TranscriptionResult {
        val apiKey = apiKeyRepository.getApiKey(ApiKeySlots.STT)
            ?: return TranscriptionResult.Failure.Unauthorized
        val model = providerConfigRepository.observe().first().sttModel.ifBlank { DEFAULT_MODEL }

        return withContext(Dispatchers.IO) {
            try {
                val body = MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("model", model)
                    .addFormDataPart("language", request.languageCode)
                    .addFormDataPart(
                        "file",
                        request.audioFile.name,
                        request.audioFile.asRequestBody("audio/m4a".toMediaType()),
                    )
                    .build()
                val httpRequest = Request.Builder()
                    .url(ENDPOINT)
                    .header("Authorization", "Bearer $apiKey")
                    .post(body)
                    .build()

                httpClient.newCall(httpRequest).execute().use { response ->
                    mapResponse(response.code, response.body?.string().orEmpty(), model)
                }
            } catch (e: SocketTimeoutException) {
                TranscriptionResult.Failure.Timeout
            } catch (e: IOException) {
                TranscriptionResult.Failure.ServerError(e)
            }
        }
    }

    internal fun mapResponse(httpCode: Int, responseBody: String, model: String): TranscriptionResult = when (httpCode) {
        200 -> {
            val text = runCatching {
                Json.parseToJsonElement(responseBody).jsonObject["text"]?.jsonPrimitive?.contentOrNull
            }.getOrNull()
            if (text.isNullOrBlank()) {
                TranscriptionResult.Failure.EmptyResult
            } else {
                TranscriptionResult.Success(text = text, providerName = "openai", modelName = model)
            }
        }
        401, 403 -> TranscriptionResult.Failure.Unauthorized
        413 -> TranscriptionResult.Failure.FileTooLarge
        429 -> TranscriptionResult.Failure.RateLimited
        in 500..599 -> TranscriptionResult.Failure.ServerError()
        else -> TranscriptionResult.Failure.ServerError()
    }
}
