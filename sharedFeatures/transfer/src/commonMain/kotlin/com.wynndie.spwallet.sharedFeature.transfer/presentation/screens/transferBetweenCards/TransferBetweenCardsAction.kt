package com.wynndie.spwallet.sharedFeature.transfer.presentation.screens.transferBetweenCards

import androidx.compose.ui.text.input.TextFieldValue

sealed interface TransferBetweenCardsAction {
    data class OnSwipeSourceCardsCarousel(val index: Int) : TransferBetweenCardsAction
    data class OnSwipeDestinationCardsCarousel(val index: Int) : TransferBetweenCardsAction

    data object OnToggleCalculatorSheet : TransferBetweenCardsAction

    data object OnClickBack : TransferBetweenCardsAction
    data object OnClickTransferAction : TransferBetweenCardsAction

    data class OnChangeTransferAmountValueAction(val value: TextFieldValue) : TransferBetweenCardsAction
}