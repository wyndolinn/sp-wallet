package com.wynndie.spwallet.sharedCore.presentation.states

import com.wynndie.spwallet.sharedCore.presentation.formatters.UiText

sealed interface LoadingState {
    data object Loading : LoadingState
    data object Finished : LoadingState
    data class Failed(val message: UiText) : LoadingState
}