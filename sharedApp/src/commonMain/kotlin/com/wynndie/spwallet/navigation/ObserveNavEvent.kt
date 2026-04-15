package com.wynndie.spwallet.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.wynndie.spwallet.sharedCore.presentation.controllers.navigation.NavEvent
import com.wynndie.spwallet.sharedCore.presentation.controllers.navigation.NavEventController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.withContext
import org.koin.compose.koinInject

@Composable
inline fun <reified T : NavEvent> ObserveNavEvent(
    noinline onEvent: (T) -> Unit
) {
    val navEventController = koinInject<NavEventController>()
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner.lifecycle, onEvent) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                navEventController.navEvent.filterIsInstance<T>().collect(onEvent)
            }
        }
    }
}