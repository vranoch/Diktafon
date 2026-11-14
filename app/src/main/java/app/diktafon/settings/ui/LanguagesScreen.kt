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
import app.diktafon.ui.components.Caption
import app.diktafon.ui.components.GlyphIconButton
import app.diktafon.ui.components.PrimaryActionButton
import app.diktafon.ui.components.SectionLabel

@Composable
fun LanguagesScreen(
    onBack: () -> Unit,
    onHome: (() -> Unit)? = null,
    viewModel: LanguagesViewModel = hiltViewModel(),
) {
    val languages by viewModel.languages().collectAsState(initial = emptyList())
    var newCode by remember { mutableStateOf("") }
    var newName by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp)) {
        AppHeader(title = "Jazyky přepisu", onBack = onBack, onHome = onHome)
        Spacer(Modifier.height(8.dp))
        Caption("Jazyky, které se nabízejí k výběru na obrazovce Přepis a jako výchozí jazyk v Administraci.")
        Spacer(Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = newCode,
                onValueChange = { newCode = it },
                label = { Text("Kód (např. cs)") },
                modifier = Modifier.weight(0.4f),
            )
            Spacer(Modifier.width(8.dp))
            OutlinedTextField(
                value = newName,
                onValueChange = { newName = it },
                label = { Text("Název (např. Čeština)") },
                modifier = Modifier.weight(0.6f),
            )
        }
        Spacer(Modifier.height(10.dp))
        PrimaryActionButton(
            text = "Přidat jazyk",
            onClick = { viewModel.add(newCode, newName); newCode = ""; newName = "" },
        )
        Spacer(Modifier.height(20.dp))

        SectionLabel("Povolené jazyky")
        Spacer(Modifier.height(8.dp))
        for (language in languages) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                Text("${language.displayName} (${language.code})", modifier = Modifier.weight(1f))
                GlyphIconButton(glyph = "🗑", contentDescription = "Smazat jazyk ${language.displayName}", onClick = { viewModel.remove(language.code) })
            }
        }
    }
}
