package com.wynndie.spwallet.sharedFeature.home.presentation.screen.home

import com.wynndie.spwallet.sharedCore.presentation.model.LoadingState
import com.wynndie.spwallet.sharedCore.presentation.model.input.InputFieldState
import com.wynndie.spwallet.sharedFeature.home.domain.constants.Constants
import com.wynndie.spwallet.sharedFeature.home.domain.model.AuthedUser
import com.wynndie.spwallet.sharedFeature.home.presentation.model.BlocksDisplayableValue
import com.wynndie.spwallet.sharedFeature.home.presentation.model.UiAuthedCard
import com.wynndie.spwallet.sharedFeature.home.presentation.model.UiCashCard
import com.wynndie.spwallet.sharedFeature.home.presentation.model.UiUnauthedCard

data class HomeState(
    val screenLoadingState: LoadingState = LoadingState.Finished,
    val authLoadingState: LoadingState = LoadingState.Finished,

    val carouselPage: Int = 0,

    val isAuthCardSheetVisible: Boolean = false,
    val isAuthedCardSheetVisible: Boolean = false,
    val isDeactivateCardDialogVisible: Boolean = false,

    val isAuthButtonEnabled: Boolean = false,

    val authedUser: AuthedUser = AuthedUser("", ""),
    val totalBalance: BlocksDisplayableValue = BlocksDisplayableValue.of(0L),

    val areCashCardsVisible: Boolean = true,
    val cashCards: List<UiCashCard> = emptyList(),
    val authedCards: List<UiAuthedCard> = emptyList(),
    val unauthedCards: List<UiUnauthedCard> = emptyList(),

    val idInputFieldState: InputFieldState = InputFieldState(
        maxLength = Constants.UUID_LENGTH
    ),
    val tokenInputFieldState: InputFieldState = InputFieldState(
        maxLength = Constants.TOKEN_LENGTH
    )
)

