package app.diktafon.structuring.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.diktafon.structuring.domain.Template

@Composable
fun TemplateListScreen(
    onBack: () -> Unit,
    onCreateNew: () -> Unit,
    onEdit: (Template) -> Unit,
    viewModel: TemplateListViewModel = hiltViewModel(),
) {
    val templates by viewModel.templates().collectAsState(initial = emptyList())

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Button(onClick = onBack) { Text("Zpět") }
        Text("Správa šablon")
        Button(onClick = onCreateNew) { Text("Nová šablona") }
        for (template in templates) {
            Button(onClick = { onEdit(template) }) { Text("${template.name} (v${template.version})") }
        }
    }
}
