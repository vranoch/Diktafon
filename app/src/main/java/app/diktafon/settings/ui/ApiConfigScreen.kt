package app.diktafon.settings.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
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
import app.diktafon.settings.domain.ProviderNames
import app.diktafon.ui.components.AppHeader
import app.diktafon.ui.components.Caption
import app.diktafon.ui.components.PrimaryActionButton
import app.diktafon.ui.components.SecondaryActionButton
import app.diktafon.ui.components.SectionCard
import app.diktafon.ui.components.SectionLabel
import app.diktafon.ui.components.StatusBadge
import app.diktafon.ui.theme.DiktafonAccentContainer
import app.diktafon.ui.theme.DiktafonAccentContainerOn
import app.diktafon.ui.theme.DiktafonDangerContainer
import app.diktafon.ui.theme.DiktafonDangerContainerOn
import app.diktafon.ui.theme.DiktafonOutline
import app.diktafon.ui.theme.DiktafonWarningContainer
import app.diktafon.ui.theme.DiktafonWarningContainerOn

@Composable
fun ApiConfigScreen(
    onBack: (() -> Unit)? = null,
    onHome: (() -> Unit)? = null,
    viewModel: ApiConfigViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()
    val availableLanguages by viewModel.availableLanguages().collectAsState(initial = emptyList())
    var sttKeyInput by remember { mutableStateOf("") }
    var llmKeyInput by remember { mutableStateOf("") }
    var sttProviderInput by remember(state.sttProviderName) { mutableStateOf(state.sttProviderName) }
    var sttModelInput by remember(state.sttModel) { mutableStateOf(state.sttModel) }
    var llmProviderInput by remember(state.llmProviderName) { mutableStateOf(state.llmProviderName) }
    var llmModelInput by remember(state.llmModel) { mutableStateOf(state.llmModel) }
    var languageInput by remember(state.defaultLanguageCode) { mutableStateOf(state.defaultLanguageCode) }

    LaunchedEffect(Unit) { viewModel.load() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 8.dp),
    ) {
        AppHeader(title = "Připojení", onBack = onBack, onHome = onHome)
        Spacer(Modifier.height(12.dp))
        Caption(
            "U Simulace není potřeba API klíč — přepis i zpracování šablonou jen předstírají " +
                "výsledek. OpenAI volá skutečnou službu a potřebuje platný klíč.",
        )
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
            Spacer(Modifier.height(10.dp))
            ProviderSelector(selected = sttProviderInput, onSelect = { sttProviderInput = it })
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = sttModelInput, onValueChange = { sttModelInput = it }, label = { Text("Model (např. whisper-1)") }, modifier = Modifier.fillMaxWidth())
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
            Spacer(Modifier.height(10.dp))
            ProviderSelector(selected = llmProviderInput, onSelect = { llmProviderInput = it })
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = llmModelInput, onValueChange = { llmModelInput = it }, label = { Text("Model (např. gpt-4o-mini)") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(10.dp))
            SecondaryActionButton(text = "Uložit LLM klíč", onClick = { viewModel.saveLlmApiKey(llmKeyInput); llmKeyInput = "" })
            Spacer(Modifier.height(8.dp))
            SecondaryActionButton(text = "Smazat LLM klíč", onClick = viewModel::clearLlmApiKey)
        }
        Spacer(Modifier.height(16.dp))

        SectionLabel("Výchozí jazyk přepisu")
        Spacer(Modifier.height(8.dp))
        if (availableLanguages.isEmpty()) {
            Caption("Žádný jazyk není povolený — přidejte jej v Administraci v sekci „Jazyky přepisu“.")
        } else {
            Row {
                availableLanguages.forEachIndexed { index, language ->
                    if (index > 0) Spacer(Modifier.width(8.dp))
                    val selected = languageInput == language.code
                    if (selected) {
                        Button(
                            onClick = { languageInput = language.code },
                            modifier = Modifier.weight(1f).height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DiktafonAccentContainer,
                                contentColor = DiktafonAccentContainerOn,
                            ),
                        ) { Text("✓ ${language.displayName}") }
                    } else {
                        OutlinedButton(
                            onClick = { languageInput = language.code },
                            modifier = Modifier.weight(1f).height(48.dp),
                            border = BorderStroke(1.5.dp, DiktafonOutline),
                        ) { Text(language.displayName) }
                    }
                }
            }
        }
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

@Composable
private fun ProviderSelector(selected: String, onSelect: (String) -> Unit) {
    val isOpenAi = selected.equals(ProviderNames.OPENAI, ignoreCase = true)
    Row {
        ProviderChip(
            label = "Simulace",
            selected = !isOpenAi,
            onClick = { onSelect(ProviderNames.MOCK) },
            modifier = Modifier.weight(1f),
        )
        Spacer(Modifier.width(8.dp))
        ProviderChip(
            label = "OpenAI",
            selected = isOpenAi,
            onClick = { onSelect(ProviderNames.OPENAI) },
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun ProviderChip(label: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    if (selected) {
        Button(
            onClick = onClick,
            modifier = modifier.height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = DiktafonAccentContainer,
                contentColor = DiktafonAccentContainerOn,
            ),
        ) { Text("✓ $label") }
    } else {
        OutlinedButton(
            onClick = onClick,
            modifier = modifier.height(48.dp),
            border = BorderStroke(1.5.dp, DiktafonOutline),
        ) { Text(label) }
    }
}
