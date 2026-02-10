package com.wynndie.spwallet.sharedFeature.transfer.presentation.screens.searchRecipient

import androidx.compose.ui.text.input.TextFieldValue

sealed interface SearchRecipientAction {

    data object OnClickBack : SearchRecipientAction
    data object OnClickAddRecipient: SearchRecipientAction

    data class OnChangeRecipientValue(
        val value: TextFieldValue
    ) : SearchRecipientAction

    data class OnClickRecipient(
        val cardNumber: String
    ) : SearchRecipientAction
}