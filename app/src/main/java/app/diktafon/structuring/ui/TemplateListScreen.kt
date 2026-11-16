package app.diktafon.structuring.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import app.diktafon.structuring.domain.Template
import app.diktafon.ui.components.AppHeader
import app.diktafon.ui.components.Caption
import app.diktafon.ui.components.GlyphIconButton
import app.diktafon.ui.components.PrimaryActionButton
import app.diktafon.ui.components.SecondaryActionButton
import app.diktafon.ui.components.SectionLabel

private data class VersionPendingDelete(val templateId: Long, val version: Int, val templateName: String)

@Composable
fun TemplateListScreen(
    onBack: () -> Unit,
    onHome: (() -> Unit)? = null,
    onCreateNew: () -> Unit,
    onEdit: (Template) -> Unit,
    viewModel: TemplateListViewModel = hiltViewModel(),
) {
    val templates by viewModel.templates().collectAsState(initial = emptyList())
    var templatePendingDelete by remember { mutableStateOf<Template?>(null) }
    var versionPendingDelete by remember { mutableStateOf<VersionPendingDelete?>(null) }
    var expandedTemplateId by remember { mutableStateOf<Long?>(null) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 8.dp),
    ) {
        AppHeader(title = "Správa šablon", onBack = onBack, onHome = onHome)
        Spacer(Modifier.height(12.dp))
        PrimaryActionButton(text = "Nová šablona", onClick = onCreateNew)
        Spacer(Modifier.height(16.dp))

        SectionLabel("Existující šablony")
        Spacer(Modifier.height(8.dp))
        for (template in templates) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                SecondaryActionButton(
                    text = "${template.name} (v${template.version})",
                    onClick = { onEdit(template) },
                    modifier = Modifier.weight(1f),
                )
                Spacer(Modifier.width(8.dp))
                GlyphIconButton(
                    glyph = "🗑",
                    contentDescription = "Smazat všechny verze šablony ${template.name}",
                    onClick = { templatePendingDelete = template },
                )
            }
            TextButton(onClick = {
                expandedTemplateId = if (expandedTemplateId == template.id) null else template.id
            }) {
                Text(if (expandedTemplateId == template.id) "Skrýt verze" else "Zobrazit verze")
            }

            if (expandedTemplateId == template.id) {
                val versions by viewModel.versions(template.id).collectAsState(initial = emptyList())
                Column(modifier = Modifier.padding(start = 16.dp)) {
                    for (version in versions) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) {
                            Caption("v${version.version} · ${version.fields.size} polí", modifier = Modifier.weight(1f))
                            GlyphIconButton(
                                glyph = "🗑",
                                contentDescription = "Smazat verzi ${version.version} šablony ${template.name}",
                                onClick = {
                                    versionPendingDelete = VersionPendingDelete(template.id, version.version, template.name)
                                },
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(4.dp))
        }
    }

    templatePendingDelete?.let { template ->
        AlertDialog(
            onDismissRequest = { templatePendingDelete = null },
            title = { Text("Smazat šablonu „${template.name}“?") },
            text = {
                Text(
                    "Smažou se všechny verze této šablony. Už vytvořené dokumenty touto " +
                        "šablonou zpracované zůstanou beze změny. Tuto akci nelze vrátit zpět.",
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.delete(template.id)
                    templatePendingDelete = null
                }) {
                    Text("Smazat", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { templatePendingDelete = null }) { Text("Zrušit") }
            },
        )
    }

    versionPendingDelete?.let { pending ->
        AlertDialog(
            onDismissRequest = { versionPendingDelete = null },
            title = { Text("Smazat verzi ${pending.version} šablony „${pending.templateName}“?") },
            text = {
                Text(
                    "Smaže se jen tato jedna verze, ostatní verze šablony zůstanou zachované. " +
                        "Už vytvořené dokumenty touto verzí zpracované zůstanou beze změny. " +
                        "Tuto akci nelze vrátit zpět.",
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteVersion(pending.templateId, pending.version)
                    versionPendingDelete = null
                }) {
                    Text("Smazat", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { versionPendingDelete = null }) { Text("Zrušit") }
            },
        )
    }
}
