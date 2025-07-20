package com.wynndie.spwallet.sharedFeature.transfer.presentation.screen.searchRecipient

import androidx.compose.ui.text.input.TextFieldValue

sealed interface SearchRecipientAction {

    data class OnChangeRecipientValue(val value: TextFieldValue) : SearchRecipientAction
    data class OnClickEditRecipient(val recipientId: String) : SearchRecipientAction
    data class OnClickRecipient(
        val id: String?,
        val cardNumber: String?
    ) : SearchRecipientAction
}