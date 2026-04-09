package com.wynndie.spwallet.sharedFeature.edit.presentation.screens.customCard

import androidx.compose.ui.text.input.TextFieldValue

sealed interface CustomCardAction {
    data object OnToggleCustomizationSheet : CustomCardAction
    data object OnToggleCalculatorSheet : CustomCardAction
    data object OnToggleDeleteDialog : CustomCardAction

    data object OnClickDeleteCard : CustomCardAction
    data object OnClickBack : CustomCardAction
    data class OnClickColorChip(val value: Int) : CustomCardAction
    data class OnClickSaveCard(
        val cardName: String,
        val cardBalance: String,
    ) : CustomCardAction

    data class OnChangeNameValue(val value: TextFieldValue) : CustomCardAction
    data class OnChangeBalanceValue(val value: TextFieldValue) : CustomCardAction

    data object OnToggleNameFocus : CustomCardAction
    data object OnToggleBalanceFocus : CustomCardAction
}