package com.wynndie.spwallet.sharedFeature.transfer.presentation.screens.transferByCard

import androidx.compose.ui.text.input.TextFieldValue

sealed interface TransferByCardAction {
    data class OnSwipeCarousel(val index: Int) : TransferByCardAction

    data object OnToggleCalculatorSheet : TransferByCardAction

    data object OnClickBack : TransferByCardAction
    data object OnClickRecipient : TransferByCardAction
    data class OnClickTransfer(
        val cardNumber: String,
        val transferAmount: String,
        val comment: String
    ) : TransferByCardAction

    data class OnChangeTransferAmountValue(val value: TextFieldValue) : TransferByCardAction
    data class OnChangeCommentValue(val value: TextFieldValue) : TransferByCardAction

    data object OnToggleTransferAmountFocus : TransferByCardAction
    data object OnToggleCommentFocus : TransferByCardAction
}