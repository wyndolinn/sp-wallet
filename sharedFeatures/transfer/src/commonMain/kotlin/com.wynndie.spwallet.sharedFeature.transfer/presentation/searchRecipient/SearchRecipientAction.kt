package com.wynndie.spwallet.sharedFeature.transfer.presentation.searchRecipient

import androidx.compose.ui.text.input.TextFieldValue

sealed interface SearchRecipientAction {
    data object NavigateBack : SearchRecipientAction
    data class ChangeRecipientValue(val value: TextFieldValue) : SearchRecipientAction
    data class SelectRecipient(val cardNumber: String) : SearchRecipientAction
}