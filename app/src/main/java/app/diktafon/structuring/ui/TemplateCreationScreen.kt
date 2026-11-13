package app.diktafon.structuring.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun TemplateCreationScreen(
    existingTranscriptForTesting: String?,
    viewModel: TemplateCreationViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        OutlinedTextField(
            value = state.draftName,
            onValueChange = viewModel::onDraftNameChanged,
            label = { Text("Název šablony") },
        )
        OutlinedTextField(
            value = state.exampleText,
            onValueChange = viewModel::onExampleTextChanged,
            label = { Text("Vzorový text výsledného dokumentu") },
        )
        OutlinedTextField(
            value = state.fieldNotes,
            onValueChange = viewModel::onFieldNotesChanged,
            label = { Text("Poznámky k polím (volitelné, oddělené čárkou)") },
        )
        Button(onClick = viewModel::generateDraft) { Text("Odvodit draft šablony") }

        if (state.draftFields.isNotEmpty()) {
            Text("Draft pole: ${state.draftFields.joinToString { it.name }}")

            if (existingTranscriptForTesting != null) {
                Button(onClick = { viewModel.testDraftOnTranscript(existingTranscriptForTesting) }) {
                    Text("Otestovat na existujícím přepisu")
                }
            }

            if (state.testResultFields.isNotEmpty()) {
                Text("Výsledek testu:")
                for ((name, value) in state.testResultFields) {
                    Text("$name: ${value ?: "(chybí)"}")
                }
            }

            Button(onClick = { viewModel.saveAsNewVersion() }) { Text("Uložit jako novou verzi") }
        }

        state.savedVersion?.let { Text("Uloženo jako verze $it.") }
        state.errorMessage?.let { Text(it) }
    }
}
