package com.wynndie.spwallet

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.wynndie.spwallet.navigation.Route
import com.wynndie.spwallet.navigation.rootNavGraph.RootNavHost
import com.wynndie.spwallet.sharedCore.presentation.components.effects.ObserveAsEvents
import com.wynndie.spwallet.sharedCore.presentation.controllers.overlay.OverlayController
import com.wynndie.spwallet.sharedCore.presentation.controllers.overlay.OverlayType
import com.wynndie.spwallet.sharedtheme.theme.AppTheme
import kotlinx.coroutines.launch

@Composable
fun App() {

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }


    ObserveAsEvents(OverlayController.overlay) { overlay ->
        when (overlay) {
            is OverlayType.Snackbar -> {
                scope.launch {
                    val currentSnackbarData = snackbarHostState.currentSnackbarData
                    currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(
                        message = overlay.message.asStringAsync(),
                        actionLabel = overlay.actionLabel?.asStringAsync(),
                        withDismissAction = overlay.withDismissAction,
                        duration = overlay.duration
                    )
                }
            }
        }
    }


    AppTheme {
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState) {
                    Snackbar(snackbarData = it)
                }
            }
        ) { _ ->
            RootNavHost(
                startDestination = Route.HomeNavGraph
            )
        }
    }
}