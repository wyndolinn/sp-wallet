package com.wynndie.spwallet.sharedCore.presentation.controllers.overlay

import com.wynndie.spwallet.sharedCore.presentation.models.UiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class SnackbarController {
    private val _overlay = Channel<Snackbar>()
    val overlay = _overlay.receiveAsFlow()

    suspend fun send(text: UiText) {
        _overlay.send(Snackbar(text))
    }

    suspend fun send(snackbar: Snackbar) {
        _overlay.send(snackbar)
    }
}