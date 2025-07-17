package com.wynndie.spwallet.sharedFeature.home.presentation.screen.transfer_by_card

import com.wynndie.spwallet.sharedCore.presentation.model.input.InputFieldState
import com.wynndie.spwallet.sharedCore.presentation.model.LoadingState
import com.wynndie.spwallet.sharedCore.domain.constants.Constants
import com.wynndie.spwallet.sharedCore.domain.model.AuthedUser
import com.wynndie.spwallet.sharedCore.presentation.model.UiAuthedCard
import com.wynndie.spwallet.sharedCore.presentation.model.UiRecipientCard
import com.wynndie.spwallet.sharedCore.presentation.model.emptyRecipientCard

data class TransferByCardState(
    val loadingState: LoadingState = LoadingState.Finished,

    val user: AuthedUser = AuthedUser(
        id = "",
        name = ""
    ),

    val recipient: UiRecipientCard = emptyRecipientCard,

    val cards: List<UiAuthedCard> = emptyList(),

    val carouselPage: Int = 0,
    val isCalculatorSheetVisible: Boolean = false,
    val isChangeRecipientSheetVisible: Boolean = false,

    val recipientInputFieldState: InputFieldState = InputFieldState(
        maxLength = Constants.CARD_NUMBER_LENGTH
    ),
    val amountInputFieldState: InputFieldState = InputFieldState(
        maxLength = Constants.MAX_BALANCE_LENGTH
    ),
    val commentInputFieldState: InputFieldState = InputFieldState(
        maxLength = Constants.MAX_COMMENT_LENGTH
    )
)
