package app.diktafon.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.diktafon.settings.domain.ApiKeyRepository
import app.diktafon.settings.domain.ProviderConfig
import app.diktafon.settings.domain.ProviderConfigRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApiConfigViewModel @Inject constructor(
    private val apiKeyRepository: ApiKeyRepository,
    private val providerConfigRepository: ProviderConfigRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ApiConfigUiState())
    val uiState: StateFlow<ApiConfigUiState> = _uiState.asStateFlow()

    fun load() {
        viewModelScope.launch {
            providerConfigRepository.observe().collect { config ->
                _uiState.update {
                    it.copy(
                        sttProviderName = config.sttProviderName,
                        sttModel = config.sttModel,
                        llmProviderName = config.llmProviderName,
                        llmModel = config.llmModel,
                        defaultLanguageCode = config.defaultLanguageCode,
                        hasSttApiKey = apiKeyRepository.hasApiKey(config.sttProviderName.ifBlank { STT_KEY_SLOT }),
                        hasLlmApiKey = apiKeyRepository.hasApiKey(config.llmProviderName.ifBlank { LLM_KEY_SLOT }),
                    )
                }
            }
        }
    }

    fun saveConfig(
        sttProviderName: String,
        sttModel: String,
        llmProviderName: String,
        llmModel: String,
        defaultLanguageCode: String,
    ) {
        viewModelScope.launch {
            providerConfigRepository.save(
                ProviderConfig(
                    sttProviderName = sttProviderName,
                    sttModel = sttModel,
                    llmProviderName = llmProviderName,
                    llmModel = llmModel,
                    defaultLanguageCode = defaultLanguageCode,
                ),
            )
        }
    }

    fun saveSttApiKey(apiKey: String) = saveApiKey(STT_KEY_SLOT, apiKey, isStt = true)

    fun saveLlmApiKey(apiKey: String) = saveApiKey(LLM_KEY_SLOT, apiKey, isStt = false)

    private fun saveApiKey(slot: String, apiKey: String, isStt: Boolean) {
        if (apiKey.isBlank()) {
            _uiState.update { it.copy(errorMessage = "API klíč nesmí být prázdný.") }
            return
        }
        viewModelScope.launch {
            apiKeyRepository.saveApiKey(slot, apiKey)
            _uiState.update {
                if (isStt) {
                    it.copy(hasSttApiKey = true, statusMessage = "STT API klíč uložen.", errorMessage = null)
                } else {
                    it.copy(hasLlmApiKey = true, statusMessage = "LLM API klíč uložen.", errorMessage = null)
                }
            }
        }
    }

    fun clearSttApiKey() = clearApiKey(STT_KEY_SLOT, isStt = true)

    fun clearLlmApiKey() = clearApiKey(LLM_KEY_SLOT, isStt = false)

    private fun clearApiKey(slot: String, isStt: Boolean) {
        viewModelScope.launch {
            apiKeyRepository.clearApiKey(slot)
            _uiState.update {
                if (isStt) it.copy(hasSttApiKey = false) else it.copy(hasLlmApiKey = false)
            }
        }
    }

    companion object {
        private const val STT_KEY_SLOT = "stt"
        private const val LLM_KEY_SLOT = "llm"
    }
}
