package com.wynndie.spwallet.sharedFeature.edit.presentation.screens.customCard

import androidx.compose.ui.text.input.TextFieldValue
import com.wynndie.spwallet.sharedCore.domain.models.cards.CustomCard

sealed interface CustomCardAction {

    data object NavigateBack : CustomCardAction

    data class DeleteCard(val card: CustomCard) : CustomCardAction
    data class SaveCard(val card: CustomCard) : CustomCardAction

    data class SelectColor(val value: Int) : CustomCardAction
    data class ChangeNameValue(val value: TextFieldValue) : CustomCardAction
    data class ChangeBalanceValue(val value: TextFieldValue) : CustomCardAction

    data object ClearNameFocus : CustomCardAction
    data object ClearBalanceFocus : CustomCardAction

    data class ToggleCustomizationSheet(val open: Boolean) : CustomCardAction
    data class ToggleDeleteDialog(val open: Boolean) : CustomCardAction
}