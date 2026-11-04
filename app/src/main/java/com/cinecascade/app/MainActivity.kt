package com.cinecascade.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.cinecascade.app.ui.DiscoveryScreen
import com.cinecascade.app.ui.theme.CineCascadeTheme
import com.cinecascade.app.viewmodel.MovieDiscoveryViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        val repository = (application as CineCascadeApp).repository
        setContent {
            CineCascadeTheme {
                val vm: MovieDiscoveryViewModel = viewModel(
                    factory = viewModelFactory { initializer { MovieDiscoveryViewModel(repository) } },
                )
                DiscoveryRoute(vm)
            }
        }
    }
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
