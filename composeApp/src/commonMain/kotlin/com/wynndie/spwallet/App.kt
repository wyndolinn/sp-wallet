package com.wynndie.spwallet

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wynndie.spwallet.navigation.navhost.RootNavHost
import com.wynndie.spwallet.sharedCore.presentation.component.effect.ObserveAsEvents
import com.wynndie.spwallet.sharedCore.presentation.controller.dialog.Dialog
import com.wynndie.spwallet.sharedCore.presentation.controller.dialog.DialogController
import com.wynndie.spwallet.sharedCore.presentation.theme.AppTheme
import kotlinx.coroutines.launch
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {

    val viewModel: AppViewModel = koinViewModel()

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    ObserveAsEvents(DialogController.event) { dialog ->
        viewModel.sendDialogEvent(dialog)
    }

    when (val dialog = viewModel.dialogEvent.collectAsStateWithLifecycle().value) {
        is Dialog.AlertDialog -> {
            viewModel.sendDialogEvent(null)
        }

        is Dialog.BottomSheet -> {
            viewModel.sendDialogEvent(null)
        }

        is Dialog.Snackbar -> {
            val message = dialog.message.asString()

            scope.launch {
                snackbarHostState.showSnackbar(message = message)
                viewModel.sendDialogEvent(null)
            }
        }

        null -> Unit
    }

    AppTheme {
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            }
        ) { _ ->
            RootNavHost()
        }
    }
}