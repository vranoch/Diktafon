package app.diktafon.structuring.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.diktafon.structuring.domain.Template
import app.diktafon.ui.components.AppHeader
import app.diktafon.ui.components.PrimaryActionButton
import app.diktafon.ui.components.SecondaryActionButton
import app.diktafon.ui.components.SectionLabel

@Composable
fun TemplateListScreen(
    onBack: () -> Unit,
    onHome: (() -> Unit)? = null,
    onCreateNew: () -> Unit,
    onEdit: (Template) -> Unit,
    viewModel: TemplateListViewModel = hiltViewModel(),
) {
    val templates by viewModel.templates().collectAsState(initial = emptyList())

    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp)) {
        AppHeader(title = "Správa šablon", onBack = onBack, onHome = onHome)
        Spacer(Modifier.height(12.dp))
        PrimaryActionButton(text = "Nová šablona", onClick = onCreateNew)
        Spacer(Modifier.height(16.dp))

        SectionLabel("Existující šablony")
        Spacer(Modifier.height(8.dp))
        for (template in templates) {
            SecondaryActionButton(
                text = "${template.name} (v${template.version})",
                onClick = { onEdit(template) },
                modifier = Modifier.padding(vertical = 4.dp),
            )
        }
    }
}
