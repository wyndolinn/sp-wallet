package com.wynndie.spwallet.sharedCore.presentation.states

import com.wynndie.spwallet.sharedCore.presentation.formatters.UiText
import kotlinx.serialization.Serializable

@Serializable
sealed interface LoadingState {
    @Serializable data object Loading : LoadingState
    @Serializable data object Finished : LoadingState
    @Serializable data class Failed(val message: UiText) : LoadingState
}