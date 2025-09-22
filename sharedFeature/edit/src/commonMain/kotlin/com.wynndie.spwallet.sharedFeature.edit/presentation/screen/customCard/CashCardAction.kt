package com.wynndie.spwallet.sharedFeature.edit.presentation.screen.customCard

import androidx.compose.ui.text.input.TextFieldValue

sealed interface CashCardAction {
    data object OnToggleCustomizationSheet : CashCardAction
    data object OnToggleCalculatorSheet : CashCardAction
    data object OnToggleDeleteDialog : CashCardAction

    data object OnClickDeleteCard : CashCardAction
    data object OnClickBack : CashCardAction
    data class OnClickColorChip(val value: Int) : CashCardAction
    data class OnClickSaveCard(
        val cardName: String,
        val cardBalance: String,
    ) : CashCardAction

    data class OnChangeNameValue(val value: TextFieldValue) : CashCardAction
    data class OnChangeBalanceValue(val value: TextFieldValue) : CashCardAction
}