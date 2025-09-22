package com.wynndie.spwallet.sharedFeature.transfer.presentation.screen.transferBetweenCards

import com.wynndie.spwallet.sharedCore.domain.constants.Constants
import com.wynndie.spwallet.sharedCore.domain.model.AuthedUser
import com.wynndie.spwallet.sharedCore.presentation.model.LoadingState
import com.wynndie.spwallet.sharedCore.presentation.model.card.UiAuthedCard
import com.wynndie.spwallet.sharedCore.presentation.model.card.UiCard
import com.wynndie.spwallet.sharedCore.presentation.model.emptyAuthedUser
import com.wynndie.spwallet.sharedCore.presentation.model.input.InputFieldState

data class TransferBetweenCardsState(
    val loadingState: LoadingState = LoadingState.Finished,

    val user: AuthedUser = emptyAuthedUser,
    val sourceCards: List<UiAuthedCard> = emptyList(),
    val destinationCards: List<UiCard> = emptyList(),

    val sourceCardsCarouselPage: Int = 0,
    val destinationCardsCarouselPage: Int = 0,

    val isCalculatorSheetVisible: Boolean = false,

    val amountInputFieldState: InputFieldState = InputFieldState(
        maxLength = Constants.MAX_BALANCE_LENGTH
    )
)
