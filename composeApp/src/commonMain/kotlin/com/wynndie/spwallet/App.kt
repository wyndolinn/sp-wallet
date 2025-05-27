package com.wynndie.spwallet

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.wynndie.spwallet.navigation.navhost.RootNavHost
import com.wynndie.spwallet.sharedCore.presentation.component.effect.ObserveAsEvents
import com.wynndie.spwallet.sharedCore.presentation.controller.dialog.Dialog
import com.wynndie.spwallet.sharedCore.presentation.controller.dialog.DialogController
import com.wynndie.spwallet.sharedCore.presentation.theme.AppTheme
import kotlinx.coroutines.launch
import org.koin.compose.KoinContext

@Composable
fun App() {

    val scope = rememberCoroutineScope()
    ObserveAsEvents(DialogController.event) { dialog ->
        scope.launch {
            when (dialog) {
                is Dialog.AlertDialog -> {

                }

                is Dialog.BottomSheet -> {

                }

                is Dialog.Snackbar -> {

                }

                is Dialog.Toast -> {

                }
            }
        }
    }

    AppTheme {
        KoinContext {
            Scaffold { _ ->
                RootNavHost()
            }
        }
    }
}