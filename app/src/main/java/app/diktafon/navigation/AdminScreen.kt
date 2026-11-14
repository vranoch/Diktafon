package app.diktafon.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.diktafon.ui.components.AppHeader
import app.diktafon.ui.components.Caption
import app.diktafon.ui.components.SecondaryActionButton

@Composable
fun AdminScreen(
    onBack: () -> Unit,
    onHome: () -> Unit,
    onOpenConnection: () -> Unit,
    onOpenTemplates: () -> Unit,
    onOpenEmailRecipients: () -> Unit,
    onOpenLanguages: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp)) {
        AppHeader(title = "Administrace", onBack = onBack, onHome = onHome)
        Caption("Nastavení systému, šablon a kontaktů — odděleno od běžného použití")
        Spacer(Modifier.height(16.dp))

        SecondaryActionButton(text = "Připojení (API klíče pro STT a LLM)", onClick = onOpenConnection)
        Spacer(Modifier.height(10.dp))
        SecondaryActionButton(text = "Správa šablon", onClick = onOpenTemplates)
        Spacer(Modifier.height(10.dp))
        SecondaryActionButton(text = "E-mailoví příjemci", onClick = onOpenEmailRecipients)
        Spacer(Modifier.height(10.dp))
        SecondaryActionButton(text = "Jazyky přepisu", onClick = onOpenLanguages)
    }
}
