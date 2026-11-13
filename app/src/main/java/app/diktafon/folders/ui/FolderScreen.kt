package app.diktafon.folders.ui

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

@Composable
fun FolderScreen(viewModel: FolderViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadFolders()
        viewModel.selectFolder(null)
    }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text("Mimo složky")
        for (folder in state.folders) {
            Button(onClick = { viewModel.selectFolder(folder.id) }) {
                Text(folder.name)
            }
        }

        Text("Nahrávky ve zvolené složce: ${state.recordingsInSelectedFolder.size}")
        state.errorMessage?.let { Text(it) }
    }
}
