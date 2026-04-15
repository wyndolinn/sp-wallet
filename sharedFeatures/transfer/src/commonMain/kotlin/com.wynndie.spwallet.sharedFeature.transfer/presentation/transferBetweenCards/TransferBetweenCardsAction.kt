package com.wynndie.spwallet.sharedFeature.transfer.presentation.transferBetweenCards

import androidx.compose.ui.text.input.TextFieldValue

sealed interface TransferBetweenCardsAction {
    data object NavigateBack : TransferBetweenCardsAction
    data object MakeTransfer : TransferBetweenCardsAction
    data class SelectSourceCard(val index: Int) : TransferBetweenCardsAction
    data class SelectDestinationCard(val index: Int) : TransferBetweenCardsAction
    data class ChangeAmountValue(val value: TextFieldValue) : TransferBetweenCardsAction
    data object ClearAmountFocus : TransferBetweenCardsAction
}