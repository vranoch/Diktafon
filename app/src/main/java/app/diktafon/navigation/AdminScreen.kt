package app.diktafon.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AdminScreen(
    onBack: () -> Unit,
    onOpenConnection: () -> Unit,
    onOpenTemplates: () -> Unit,
    onOpenEmailRecipients: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Button(onClick = onBack) { Text("Zpět") }
        Text("Administrace")
        Button(onClick = onOpenConnection) { Text("Připojení (API klíče, provider, jazyk)") }
        Button(onClick = onOpenTemplates) { Text("Správa šablon") }
        Button(onClick = onOpenEmailRecipients) { Text("E-mailoví příjemci") }
    }
}
