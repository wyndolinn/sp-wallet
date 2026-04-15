package com.wynndie.spwallet.sharedFeature.edit.presentation

import androidx.compose.ui.text.input.TextFieldValue

sealed interface CustomCardAction {

    data object NavigateBack : CustomCardAction

    data object DeleteCard : CustomCardAction
    data object SaveCard : CustomCardAction

    data class SelectColor(val value: Int) : CustomCardAction
    data class ChangeNameValue(val value: TextFieldValue) : CustomCardAction
    data class ChangeBalanceValue(val value: TextFieldValue) : CustomCardAction

    data object ClearNameFocus : CustomCardAction
    data object ClearBalanceFocus : CustomCardAction

    data class ToggleCustomizationSheet(val open: Boolean) : CustomCardAction
    data class ToggleDeleteDialog(val open: Boolean) : CustomCardAction
}