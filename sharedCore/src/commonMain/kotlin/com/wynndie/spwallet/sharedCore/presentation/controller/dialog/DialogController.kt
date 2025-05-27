package com.wynndie.spwallet.sharedCore.presentation.controller.dialog

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

object DialogController {

    private val _event = Channel<Dialog>()
    val event = _event.receiveAsFlow()

    suspend fun send(event: Dialog) {
        _event.send(event)
    }
}