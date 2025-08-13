package com.wynndie.spwallet

import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.window.Popup
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wynndie.spwallet.navigation.HomeNavGraphRoutes
import com.wynndie.spwallet.navigation.TransferNavGraphRoutes
import com.wynndie.spwallet.navigation.rootNavGraph.navHost.RootNavHost
import com.wynndie.spwallet.sharedCore.presentation.component.effect.ObserveAsEvents
import com.wynndie.spwallet.sharedCore.presentation.controller.dialog.Dialog
import com.wynndie.spwallet.sharedCore.presentation.controller.dialog.DialogController
import com.wynndie.spwallet.sharedCore.presentation.theme.AppTheme
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {

    val viewModel: AppViewModel = koinViewModel()

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    ObserveAsEvents(DialogController.event) { dialog ->
        when (dialog) {
            is Dialog.AlertDialog -> {

            }

            is Dialog.BottomSheet -> {

            }

            is Dialog.Snackbar -> {
                scope.launch {
                    val currentSnackbarData = snackbarHostState.currentSnackbarData
                    currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(
                        message = dialog.message.convertAsString(),
                        withDismissAction = true,
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }

    AppTheme {
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState) {
                    Popup {
                        Snackbar(snackbarData = it)
                    }
                }
            }
        ) { _ ->
            RootNavHost(
                startDestination = HomeNavGraphRoutes.HomeNavGraph
            )
        }
    }
}