package app.diktafon.settings.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import app.diktafon.ui.components.AppHeader
import app.diktafon.ui.components.Caption
import app.diktafon.ui.components.PrimaryActionButton
import app.diktafon.ui.components.SecondaryActionButton
import app.diktafon.ui.components.SectionCard
import app.diktafon.ui.components.StatusBadge
import app.diktafon.ui.theme.DiktafonAccentContainer
import app.diktafon.ui.theme.DiktafonAccentContainerOn
import app.diktafon.ui.theme.DiktafonDangerContainer
import app.diktafon.ui.theme.DiktafonDangerContainerOn
import app.diktafon.ui.theme.DiktafonWarningContainer
import app.diktafon.ui.theme.DiktafonWarningContainerOn

@Composable
fun ApiConfigScreen(
    onBack: (() -> Unit)? = null,
    onHome: (() -> Unit)? = null,
    viewModel: ApiConfigViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()
    var sttKeyInput by remember { mutableStateOf("") }
    var llmKeyInput by remember { mutableStateOf("") }
    var sttProviderInput by remember(state.sttProviderName) { mutableStateOf(state.sttProviderName) }
    var sttModelInput by remember(state.sttModel) { mutableStateOf(state.sttModel) }
    var llmProviderInput by remember(state.llmProviderName) { mutableStateOf(state.llmProviderName) }
    var llmModelInput by remember(state.llmModel) { mutableStateOf(state.llmModel) }
    var languageInput by remember(state.defaultLanguageCode) { mutableStateOf(state.defaultLanguageCode) }

    LaunchedEffect(Unit) { viewModel.load() }

    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp)) {
        AppHeader(title = "Připojení", onBack = onBack, onHome = onHome)
        Spacer(Modifier.height(12.dp))

        SectionCard(title = "STT") {
            StatusBadge(
                text = if (state.hasSttApiKey) "Klíč nastaven" else "Klíč chybí",
                glyph = if (state.hasSttApiKey) "✓" else "!",
                containerColor = if (state.hasSttApiKey) DiktafonAccentContainer else DiktafonWarningContainer,
                contentColor = if (state.hasSttApiKey) DiktafonAccentContainerOn else DiktafonWarningContainerOn,
            )
            Spacer(Modifier.height(10.dp))
            OutlinedTextField(value = sttKeyInput, onValueChange = { sttKeyInput = it }, label = { Text("STT API klíč") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = sttProviderInput, onValueChange = { sttProviderInput = it }, label = { Text("Provider") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = sttModelInput, onValueChange = { sttModelInput = it }, label = { Text("Model") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(10.dp))
            SecondaryActionButton(text = "Uložit STT klíč", onClick = { viewModel.saveSttApiKey(sttKeyInput); sttKeyInput = "" })
            Spacer(Modifier.height(8.dp))
            SecondaryActionButton(text = "Smazat STT klíč", onClick = viewModel::clearSttApiKey)
        }
        Spacer(Modifier.height(16.dp))

        SectionCard(title = "LLM") {
            StatusBadge(
                text = if (state.hasLlmApiKey) "Klíč nastaven" else "Klíč chybí",
                glyph = if (state.hasLlmApiKey) "✓" else "!",
                containerColor = if (state.hasLlmApiKey) DiktafonAccentContainer else DiktafonWarningContainer,
                contentColor = if (state.hasLlmApiKey) DiktafonAccentContainerOn else DiktafonWarningContainerOn,
            )
            Spacer(Modifier.height(10.dp))
            OutlinedTextField(value = llmKeyInput, onValueChange = { llmKeyInput = it }, label = { Text("LLM API klíč") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = llmProviderInput, onValueChange = { llmProviderInput = it }, label = { Text("Provider") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = llmModelInput, onValueChange = { llmModelInput = it }, label = { Text("Model") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(10.dp))
            SecondaryActionButton(text = "Uložit LLM klíč", onClick = { viewModel.saveLlmApiKey(llmKeyInput); llmKeyInput = "" })
            Spacer(Modifier.height(8.dp))
            SecondaryActionButton(text = "Smazat LLM klíč", onClick = viewModel::clearLlmApiKey)
        }
        Spacer(Modifier.height(16.dp))

        // TODO (ASSUMPTION): jazyky prepisu jsou zatim jeden volne psany kod, ne spravovany
        // seznam jako v mockupu - neni podlozeno existujici FR/schematem DB, viz zprava pro uzivatele.
        OutlinedTextField(
            value = languageInput,
            onValueChange = { languageInput = it },
            label = { Text("Výchozí jazyk přepisu (kód, např. cs)") },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(12.dp))
        PrimaryActionButton(
            text = "Uložit nastavení",
            onClick = { viewModel.saveConfig(sttProviderInput, sttModelInput, llmProviderInput, llmModelInput, languageInput) },
        )

        state.statusMessage?.let {
            Spacer(Modifier.height(12.dp))
            StatusBadge(text = it, glyph = "✓", containerColor = DiktafonAccentContainer, contentColor = DiktafonAccentContainerOn)
        }
        state.errorMessage?.let {
            Spacer(Modifier.height(12.dp))
            StatusBadge(text = it, glyph = "!", containerColor = DiktafonDangerContainer, contentColor = DiktafonDangerContainerOn)
        }
    }
}
