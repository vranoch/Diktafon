package app.diktafon.settings.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ApiConfigScreen(viewModel: ApiConfigViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()
    var sttKeyInput by remember { mutableStateOf("") }
    var llmKeyInput by remember { mutableStateOf("") }
    var sttProviderInput by remember(state.sttProviderName) { mutableStateOf(state.sttProviderName) }
    var sttModelInput by remember(state.sttModel) { mutableStateOf(state.sttModel) }
    var llmProviderInput by remember(state.llmProviderName) { mutableStateOf(state.llmProviderName) }
    var llmModelInput by remember(state.llmModel) { mutableStateOf(state.llmModel) }
    var languageInput by remember(state.defaultLanguageCode) { mutableStateOf(state.defaultLanguageCode) }

    LaunchedEffect(Unit) { viewModel.load() }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("STT: ${if (state.hasSttApiKey) "klíč nastaven" else "klíč chybí"}")
        OutlinedTextField(value = sttKeyInput, onValueChange = { sttKeyInput = it }, label = { Text("STT API klíč") })
        Button(onClick = { viewModel.saveSttApiKey(sttKeyInput); sttKeyInput = "" }) { Text("Uložit STT klíč") }
        Button(onClick = viewModel::clearSttApiKey) { Text("Smazat STT klíč") }

        Text("LLM: ${if (state.hasLlmApiKey) "klíč nastaven" else "klíč chybí"}")
        OutlinedTextField(value = llmKeyInput, onValueChange = { llmKeyInput = it }, label = { Text("LLM API klíč") })
        Button(onClick = { viewModel.saveLlmApiKey(llmKeyInput); llmKeyInput = "" }) { Text("Uložit LLM klíč") }
        Button(onClick = viewModel::clearLlmApiKey) { Text("Smazat LLM klíč") }

        OutlinedTextField(value = sttProviderInput, onValueChange = { sttProviderInput = it }, label = { Text("STT provider") })
        OutlinedTextField(value = sttModelInput, onValueChange = { sttModelInput = it }, label = { Text("STT model") })
        OutlinedTextField(value = llmProviderInput, onValueChange = { llmProviderInput = it }, label = { Text("LLM provider") })
        OutlinedTextField(value = llmModelInput, onValueChange = { llmModelInput = it }, label = { Text("LLM model") })
        OutlinedTextField(value = languageInput, onValueChange = { languageInput = it }, label = { Text("Výchozí jazyk přepisu") })
        Button(
            onClick = {
                viewModel.saveConfig(sttProviderInput, sttModelInput, llmProviderInput, llmModelInput, languageInput)
            },
        ) { Text("Uložit nastavení") }

        state.statusMessage?.let { Text(it) }
        state.errorMessage?.let { Text(it) }
    }
}
