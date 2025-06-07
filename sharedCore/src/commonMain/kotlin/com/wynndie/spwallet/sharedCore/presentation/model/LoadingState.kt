package com.wynndie.spwallet.sharedCore.presentation.model

sealed interface LoadingState {
    data object Loading : LoadingState
    data object Finished : LoadingState
    data class Failed(val message: UiText) : LoadingState
}