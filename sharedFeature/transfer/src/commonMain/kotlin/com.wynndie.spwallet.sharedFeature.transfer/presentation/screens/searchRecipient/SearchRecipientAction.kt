package com.wynndie.spwallet.sharedFeature.transfer.presentation.screens.searchRecipient

import androidx.compose.ui.text.input.TextFieldValue

sealed interface SearchRecipientAction {

    data object OnClickBack : SearchRecipientAction

    data class OnChangeRecipientValue(
        val value: TextFieldValue
    ) : SearchRecipientAction

    data class OnClickEditRecipient(
        val id: String? = null,
        val cardNumber: String? = null
    ) : SearchRecipientAction

    data class OnClickRecipient(
        val cardNumber: String,
        val id: String? = null
    ) : SearchRecipientAction
}