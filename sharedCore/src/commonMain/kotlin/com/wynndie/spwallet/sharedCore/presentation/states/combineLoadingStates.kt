package com.wynndie.spwallet.sharedCore.presentation.states

import com.wynndie.spwallet.sharedCore.domain.outcome.Error
import com.wynndie.spwallet.sharedCore.presentation.extensions.asUiText

internal fun combineLoadingStates(
    vararg states: LoadingState
): LoadingState {
    return when {
        states.any { it is LoadingState.Loading } -> LoadingState.Loading

        states.any { it is LoadingState.Failed } -> {
            val failedLoadingState = states.firstOrNull {
                it is LoadingState.Failed
            } as? LoadingState.Failed

            val error = failedLoadingState?.message ?: Error.Network.UNKNOWN.asUiText()
            LoadingState.Failed(error)
        }

        states.all { it is LoadingState.Finished } -> LoadingState.Finished

        else -> LoadingState.Loading
    }
}