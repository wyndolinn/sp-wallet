package com.wynndie.spwallet.sharedFeature.wallet.presentation.screen.transfer_by_card

import androidx.compose.ui.text.input.TextFieldValue

sealed interface TransferByCardAction {
    data class OnSwipeCarousel(val index: Int) : TransferByCardAction

    data object OnToggleCalculatorSheet : TransferByCardAction
    data object OnToggleRecipientSheet : TransferByCardAction

    data class OnClickRecipient(val recipient: String) : TransferByCardAction
    data class OnClickTransfer(
        val cardNumber: String,
        val transferAmount: String,
        val comment: String,
        val navigateBack: () -> Unit
    ) : TransferByCardAction

    data class OnChangeRecipientValue(val value: TextFieldValue) : TransferByCardAction
    data class OnChangeTransferAmountValue(val value: TextFieldValue) : TransferByCardAction
    data class OnChangeCommentValue(val value: TextFieldValue) : TransferByCardAction
}