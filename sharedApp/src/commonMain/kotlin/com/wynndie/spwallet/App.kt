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
import com.wynndie.spwallet.sharedCore.presentation.components.ObserveAsEvents
import com.wynndie.spwallet.sharedCore.presentation.controllers.overlay.SnackbarController
import com.wynndie.spwallet.sharedCore.presentation.theme.AppTheme
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun App() {

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val snackbarController = koinInject<SnackbarController>()
    ObserveAsEvents(snackbarController.overlay) { snackbar ->
        scope.launch {
            val currentSnackbarData = snackbarHostState.currentSnackbarData
            currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(
                message = snackbar.message.asStringAsync(),
                actionLabel = snackbar.actionLabel?.asStringAsync(),
                withDismissAction = snackbar.withDismissAction,
                duration = snackbar.duration
            )
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