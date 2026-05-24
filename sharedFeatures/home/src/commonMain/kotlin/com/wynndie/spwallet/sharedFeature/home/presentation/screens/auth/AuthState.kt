package com.wynndie.spwallet.sharedFeature.home.presentation.screens.auth

import com.wynndie.spwallet.sharedCore.domain.models.cards.UnauthedCard
import com.wynndie.spwallet.sharedCore.presentation.formatters.InputFieldState
import com.wynndie.spwallet.sharedCore.presentation.formatters.LoadingState

data class AuthState(
    val loadingState: LoadingState = LoadingState.Finished,
    val carouselPage: Int = 0,
    val isAuthButtonEnabled: Boolean = false,

    val cards: List<UnauthedCard> = emptyList(),
    val isHelpSheetOpen: Boolean = false,

    val idInputFieldState: InputFieldState = InputFieldState(
        maxLength = 36
    ),
    val tokenInputFieldState: InputFieldState = InputFieldState(
        maxLength = 32
    )
)
