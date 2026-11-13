package app.diktafon.settings.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun EmailRecipientsScreen(
    onBack: () -> Unit,
    viewModel: EmailRecipientsViewModel = hiltViewModel(),
) {
    val recipients by viewModel.recipients().collectAsState(initial = emptyList())
    var newEmail by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Button(onClick = onBack) { Text("Zpět") }
        Text("E-mailoví příjemci")

        Row {
            OutlinedTextField(value = newEmail, onValueChange = { newEmail = it }, label = { Text("E-mailová adresa") })
            Button(onClick = { viewModel.add(newEmail); newEmail = "" }) { Text("Přidat") }
        }

        for (recipient in recipients) {
            Row {
                Text(recipient.email)
                Button(onClick = { viewModel.remove(recipient.id) }) { Text("Smazat") }
            }
        }
    }
}
