package com.wynndie.spwallet.sharedFeature.transfer.presentation.screens.transferBetweenCards

import com.wynndie.spwallet.sharedCore.domain.constants.CoreConstants
import com.wynndie.spwallet.sharedCore.domain.constants.emptyAuthedUser
import com.wynndie.spwallet.sharedCore.domain.models.AuthedUser
import com.wynndie.spwallet.sharedCore.presentation.states.InputFieldState
import com.wynndie.spwallet.sharedCore.presentation.models.cards.AuthedCardUi
import com.wynndie.spwallet.sharedCore.presentation.models.cards.CardUi
import com.wynndie.spwallet.sharedCore.presentation.states.LoadingState

data class TransferBetweenCardsState(
    val loadingState: LoadingState = LoadingState.Finished,

    val user: AuthedUser = emptyAuthedUser,
    val sourceCards: List<AuthedCardUi> = emptyList(),
    val destinationCards: List<CardUi> = emptyList(),

    val sourceCardsCarouselPage: Int = 0,
    val destinationCardsCarouselPage: Int = 0,

    val isCalculatorSheetVisible: Boolean = false,

    val amountInputFieldState: InputFieldState = InputFieldState(
        maxLength = CoreConstants.MAX_BALANCE_LENGTH
    )
)
