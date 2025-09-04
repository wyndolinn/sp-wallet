package com.wynndie.spwallet.sharedCore.domain.controller.overlay

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

object OverlayController {

    private val _overlay = Channel<OverlayType>()
    val overlay = _overlay.receiveAsFlow()

    suspend fun sendOverlay(overlayType: OverlayType) {
        _overlay.send(overlayType)
    }
}