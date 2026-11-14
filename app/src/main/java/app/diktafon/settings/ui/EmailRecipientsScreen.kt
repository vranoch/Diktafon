package app.diktafon.settings.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.diktafon.ui.components.AppHeader
import app.diktafon.ui.components.GlyphIconButton
import app.diktafon.ui.components.PrimaryActionButton
import app.diktafon.ui.components.SectionLabel

@Composable
fun EmailRecipientsScreen(
    onBack: () -> Unit,
    onHome: (() -> Unit)? = null,
    viewModel: EmailRecipientsViewModel = hiltViewModel(),
) {
    val recipients by viewModel.recipients().collectAsState(initial = emptyList())
    var newEmail by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp)) {
        AppHeader(title = "E-mailoví příjemci", onBack = onBack, onHome = onHome)
        Spacer(Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = newEmail,
                onValueChange = { newEmail = it },
                label = { Text("E-mailová adresa") },
                modifier = Modifier.weight(1f),
            )
            Spacer(Modifier.width(10.dp))
            PrimaryActionButton(text = "Přidat", onClick = { viewModel.add(newEmail); newEmail = "" }, modifier = Modifier.weight(0.6f))
        }
        Spacer(Modifier.height(16.dp))

        SectionLabel("Uložení příjemci")
        Spacer(Modifier.height(8.dp))
        for (recipient in recipients) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                Text(recipient.email, modifier = Modifier.weight(1f))
                GlyphIconButton(glyph = "🗑", contentDescription = "Smazat ${recipient.email}", onClick = { viewModel.remove(recipient.id) })
            }
        }
    }
}
