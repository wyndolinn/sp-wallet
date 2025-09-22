package com.wynndie.spwallet

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.wynndie.spwallet.navigation.HomeNavGraphRoutes
import com.wynndie.spwallet.navigation.rootNavGraph.navHost.RootNavHost
import com.wynndie.spwallet.sharedCore.presentation.component.effect.ObserveAsEvents
import com.wynndie.spwallet.sharedCore.presentation.controller.overlay.OverlayController
import com.wynndie.spwallet.sharedCore.presentation.controller.overlay.OverlayType
import com.wynndie.spwallet.sharedtheme.theme.AppTheme
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {

    val viewModel: AppViewModel = koinViewModel()

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    ObserveAsEvents(OverlayController.overlay) { overlay ->
        when (overlay) {
            is OverlayType.AlertDialog -> {

            }

            is OverlayType.BottomSheet -> {

            }

            is OverlayType.Snackbar -> {
                scope.launch {
                    val currentSnackbarData = snackbarHostState.currentSnackbarData
                    currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(
                        message = overlay.message.convertAsString(),
                        actionLabel = overlay.actionLabel?.convertAsString(),
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
                startDestination = HomeNavGraphRoutes.HomeNavGraph
            )
        }
    }
}