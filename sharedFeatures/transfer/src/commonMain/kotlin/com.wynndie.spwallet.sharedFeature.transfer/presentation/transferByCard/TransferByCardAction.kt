package com.wynndie.spwallet.sharedFeature.transfer.presentation.transferByCard

import androidx.compose.ui.text.input.TextFieldValue

sealed interface TransferByCardAction {
    data object NavigateBack : TransferByCardAction

    data object MakeTransfer : TransferByCardAction
    data object EditRecipient : TransferByCardAction
    data class SelectSourceCard(val index: Int) : TransferByCardAction

    data class ChangeAmountValue(val value: TextFieldValue) : TransferByCardAction
    data class ChangeCommentValue(val value: TextFieldValue) : TransferByCardAction

    data object ClearAmountFocus : TransferByCardAction
    data object ClearCommentFocus : TransferByCardAction
}