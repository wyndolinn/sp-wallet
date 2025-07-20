package com.wynndie.spwallet.sharedFeature.transfer.presentation.screen.transferByCard

import com.wynndie.spwallet.sharedCore.presentation.model.input.InputFieldState
import com.wynndie.spwallet.sharedCore.presentation.model.LoadingState
import com.wynndie.spwallet.sharedCore.domain.constants.Constants
import com.wynndie.spwallet.sharedCore.domain.model.AuthedUser
import com.wynndie.spwallet.sharedCore.presentation.model.card.UiAuthedCard
import com.wynndie.spwallet.sharedCore.presentation.model.UiRecipient
import com.wynndie.spwallet.sharedCore.presentation.model.emptyAuthedUser
import com.wynndie.spwallet.sharedCore.presentation.model.emptyUiRecipient

data class TransferByCardState(
    val loadingState: LoadingState = LoadingState.Finished,

    val user: AuthedUser = emptyAuthedUser,
    val recipient: UiRecipient = emptyUiRecipient,

    val cards: List<UiAuthedCard> = emptyList(),

    val carouselPage: Int = 0,
    val isCalculatorSheetVisible: Boolean = false,

    val amountInputFieldState: InputFieldState = InputFieldState(
        maxLength = Constants.MAX_BALANCE_LENGTH
    ),
    val commentInputFieldState: InputFieldState = InputFieldState(
        maxLength = Constants.MAX_COMMENT_LENGTH
    )
)
