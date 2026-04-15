package com.wynndie.spwallet.sharedCore.presentation.controllers.overlay

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class SnackbarController {
    private val _overlay = Channel<Snackbar>()
    val overlay = _overlay.receiveAsFlow()

    suspend fun send(snackbar: Snackbar) {
        _overlay.send(snackbar)
    }
}