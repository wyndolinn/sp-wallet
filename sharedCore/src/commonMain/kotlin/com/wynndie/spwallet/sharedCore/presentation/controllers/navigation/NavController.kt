package com.wynndie.spwallet.sharedCore.presentation.controllers.navigation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

object NavController {

    private val _navEvent = Channel<NavEvent>()
    val navEvent = _navEvent.receiveAsFlow()

    private var isNavigationLocked = false
    private var cooldownJob: Job? = null

    private const val NAVIGATION_COOLDOWN = 300L

    suspend fun navigate(navEvent: NavEvent, ignoreCooldown: Boolean = false) {

        if (ignoreCooldown) {
            _navEvent.send(navEvent)
            return
        }

        if (!isNavigationLocked) {
            _navEvent.send(navEvent)
            restartCooldown()
        }
    }

    private fun restartCooldown() {
        isNavigationLocked = true
        cooldownJob?.cancel()
        cooldownJob = CoroutineScope(Dispatchers.Default).launch {
            delay(NAVIGATION_COOLDOWN)
            isNavigationLocked = false
        }
    }
}