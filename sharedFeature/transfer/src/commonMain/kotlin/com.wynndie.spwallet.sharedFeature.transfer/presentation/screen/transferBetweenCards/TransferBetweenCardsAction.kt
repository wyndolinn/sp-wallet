package com.wynndie.spwallet.sharedFeature.transfer.presentation.screen.transferBetweenCards

import androidx.compose.ui.text.input.TextFieldValue

sealed interface TransferBetweenCardsAction {
    data class OnSwipeSourceCardsCarousel(val index: Int) : TransferBetweenCardsAction
    data class OnSwipeDestinationCardsCarousel(val index: Int) : TransferBetweenCardsAction

    data object OnToggleCalculatorSheet : TransferBetweenCardsAction

    data object OnClickBack : TransferBetweenCardsAction
    data class OnClickTransferAction(
        val cardNumber: String,
        val comment: String
    ) : TransferBetweenCardsAction

    data class OnChangeTransferAmountValueAction(val value: TextFieldValue) : TransferBetweenCardsAction
}