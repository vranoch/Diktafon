package app.diktafon.structuring.ui

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.diktafon.structuring.domain.Template
import app.diktafon.ui.components.AppHeader
import app.diktafon.ui.components.Caption
import app.diktafon.ui.components.PrimaryActionButton
import app.diktafon.ui.components.SecondaryActionButton
import app.diktafon.ui.components.SectionLabel
import app.diktafon.ui.components.StatusBadge
import app.diktafon.ui.theme.DiktafonAccentContainer
import app.diktafon.ui.theme.DiktafonAccentContainerOn
import app.diktafon.ui.theme.DiktafonDangerContainer
import app.diktafon.ui.theme.DiktafonDangerContainerOn

@Composable
fun TemplateCreationScreen(
    existingTranscriptForTesting: String?,
    existingTemplate: Template? = null,
    onBack: (() -> Unit)? = null,
    onHome: (() -> Unit)? = null,
    viewModel: TemplateCreationViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(existingTemplate?.id) {
        existingTemplate?.let { viewModel.loadExisting(it) }
    }

    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp)) {
        AppHeader(title = "Šablona", onBack = onBack, onHome = onHome)
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = state.draftName,
            onValueChange = viewModel::onDraftNameChanged,
            label = { Text("Název šablony") },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = state.exampleText,
            onValueChange = viewModel::onExampleTextChanged,
            label = { Text("Vzorový text výsledného dokumentu") },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = state.fieldNotes,
            onValueChange = viewModel::onFieldNotesChanged,
            label = { Text("Poznámky k polím (volitelné, oddělené čárkou)") },
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(Modifier.height(12.dp))
        SecondaryActionButton(text = "Odvodit draft šablony", onClick = viewModel::generateDraft)

        if (state.draftFields.isNotEmpty()) {
            Spacer(Modifier.height(16.dp))
            SectionLabel("Draft pole")
            Spacer(Modifier.height(6.dp))
            Caption(state.draftFields.joinToString { it.name })

            if (existingTranscriptForTesting != null) {
                Spacer(Modifier.height(10.dp))
                SecondaryActionButton(
                    text = "Otestovat na existujícím přepisu",
                    onClick = { viewModel.testDraftOnTranscript(existingTranscriptForTesting) },
                )
            }

            if (state.testResultFields.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                SectionLabel("Výsledek testu")
                Spacer(Modifier.height(6.dp))
                for ((name, value) in state.testResultFields) {
                    Caption("$name: ${value ?: "(chybí)"}")
                }
            }

            Spacer(Modifier.height(16.dp))
            PrimaryActionButton(text = "Uložit jako novou verzi", onClick = viewModel::saveAsNewVersion)
        }

        state.savedVersion?.let {
            Spacer(Modifier.height(12.dp))
            StatusBadge(text = "Uloženo jako verze $it.", glyph = "✓", containerColor = DiktafonAccentContainer, contentColor = DiktafonAccentContainerOn)
        }
        state.errorMessage?.let {
            Spacer(Modifier.height(12.dp))
            StatusBadge(text = it, glyph = "!", containerColor = DiktafonDangerContainer, contentColor = DiktafonDangerContainerOn)
        }
    }
}
