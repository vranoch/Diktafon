package app.diktafon.structuring.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.diktafon.structuring.domain.Template

@Composable
fun TemplateSuggestionScreen(
    transcriptText: String,
    approvedCatalog: List<Template>,
    onConfirmed: (Template) -> Unit,
    viewModel: TemplateSuggestionViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(transcriptText) {
        viewModel.recognizeFromTranscript(transcriptText, approvedCatalog)
    }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(
            if (state.wasRecognized) {
                "Rozpoznaná šablona: ${state.selectedTemplate?.name}"
            } else {
                "Šablona nebyla rozpoznána — zvolte ji ručně."
            },
        )
        for (template in state.approvedCatalog) {
            Button(onClick = { viewModel.selectTemplate(template) }) {
                Text(template.name)
            }
        }
        state.selectedTemplate?.let { selected ->
            Button(onClick = { onConfirmed(selected) }) {
                Text("Potvrdit a pokračovat")
            }
        }
    }
}
