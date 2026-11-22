package com.cinecascade.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.cinecascade.app.ui.DiscoveryScreen
import com.cinecascade.app.ui.theme.CineCascadeTheme
import com.cinecascade.app.update.AppUpdater
import com.cinecascade.app.viewmodel.MovieDiscoveryViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        val app = application as CineCascadeApp
        val repository = app.repository
        val updater = AppUpdater(applicationContext, app.http)
        setContent {
            CineCascadeTheme {
                val vm: MovieDiscoveryViewModel = viewModel(
                    factory = viewModelFactory { initializer { MovieDiscoveryViewModel(repository) } },
                )
                DiscoveryRoute(vm)
                UpdatePrompt(updater)
            }
        }
    }
}

/** Checks GitHub for a newer build when the app opens on Wi-Fi and offers to install it. */
@Composable
private fun UpdatePrompt(updater: AppUpdater) {
    var update by remember { mutableStateOf<AppUpdater.Update?>(null) }
    LaunchedEffect(Unit) { update = updater.checkAndDownload() }
    val ready = update ?: return
    AlertDialog(
        onDismissRequest = { update = null },
        title = { Text("New version ready") },
        text = { Text("Build ${ready.versionCode} of CineCascade has been downloaded. Install it now?") },
        confirmButton = { TextButton(onClick = { updater.install(ready) }) { Text("Install") } },
        dismissButton = { TextButton(onClick = { update = null }) { Text("Later") } },
    )
}

@Composable
private fun DiscoveryRoute(vm: MovieDiscoveryViewModel) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    val catalog by vm.catalog.collectAsStateWithLifecycle()

    // System back: close curtains first, then climb the tree.
    BackHandler(enabled = state.isFormatCurtainOpen || state.isGenreCurtainOpen || state.tree.canGoBack) {
        if (state.isFormatCurtainOpen || state.isGenreCurtainOpen) vm.onCloseCurtains() else vm.onBack()
    }

    DiscoveryScreen(state = state, catalog = catalog, actions = vm)
}
