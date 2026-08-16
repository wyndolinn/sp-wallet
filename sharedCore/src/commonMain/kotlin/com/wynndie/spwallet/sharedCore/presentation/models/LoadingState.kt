package com.wynndie.spwallet.sharedCore.presentation.formatters

import com.wynndie.spwallet.sharedCore.presentation.models.UiText

sealed interface LoadingState {
    data object Loading : LoadingState
    data object Finished : LoadingState
    data class Failed(val message: UiText) : LoadingState
}