package com.wynndie.spwallet.sharedFeature.wallet.presentation.screen.cash_card

import androidx.compose.ui.text.input.TextFieldValue

sealed interface CashCardAction {
    data object OnToggleCustomizationSheet : CashCardAction
    data object OnToggleCalculatorSheet : CashCardAction
    data object OnToggleDeleteDialog : CashCardAction

    data class OnClickColorChip(val value: Int) : CashCardAction
    data class OnClickDeleteCard(val navigateBack: () -> Unit) : CashCardAction
    data class OnClickSaveCard(
        val cardName: String,
        val cardBalance: String,
        val navigateBack: () -> Unit
    ) : CashCardAction

    data class OnChangeNameValue(val value: TextFieldValue) : CashCardAction
    data class OnChangeBalanceValue(val value: TextFieldValue) : CashCardAction
}