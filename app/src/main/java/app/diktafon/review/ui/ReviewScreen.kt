package app.diktafon.review.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ReviewScreen(
    documentId: Long,
    transcriptText: String,
    viewModel: ReviewViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(documentId) {
        viewModel.load(documentId, transcriptText)
    }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(if (state.isApproved) "SCHVÁLENO" else "NÁVRH — vyžaduje kontrolu")

        Text("Doslovný přepis (zdroj)")
        Text(state.transcriptText)

        Divider()

        Text("Strukturovaný výsledek (interpretace)")
        for ((name, value) in state.fields) {
            val isMissing = name in state.missingRequiredFields
            OutlinedTextField(
                value = value ?: "",
                onValueChange = { viewModel.onFieldChanged(name, it) },
                label = { Text(if (isMissing) "⚠ $name (chybí)" else name) },
            )
        }

        Row {
            Button(onClick = viewModel::saveChanges) { Text("Uložit změny") }
            Button(onClick = viewModel::approve) { Text("Schválit") }
        }
    }
}
