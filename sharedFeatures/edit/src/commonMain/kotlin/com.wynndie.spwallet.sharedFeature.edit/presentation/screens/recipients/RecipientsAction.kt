package com.wynndie.spwallet.sharedFeature.edit.presentation.screens.recipients

import androidx.compose.ui.text.input.TextFieldValue
import com.wynndie.spwallet.sharedCore.domain.models.cards.RecipientCard

sealed interface RecipientsAction {
    data object NavigateBack : RecipientsAction

    data class ChangeRecipientValue(val value: TextFieldValue) : RecipientsAction
    data class ChangeCardNumberValue(val value: TextFieldValue) : RecipientsAction

    data class ChangeCardNameValue(val value: TextFieldValue) : RecipientsAction
    data object ClearCardNumberFocus : RecipientsAction
    data object ClearCardNameFocus : RecipientsAction

    data class SelectRecipient(val recipient: RecipientCard?) : RecipientsAction
    data class ToggleEditRecipientSheet(val isOpen: Boolean) : RecipientsAction
    data class ToggleDeleteRecipientDialog(val isOpen: Boolean) : RecipientsAction
    data object SaveRecipient : RecipientsAction
    data object MakeTransfer : RecipientsAction
    data object DeleteRecipient : RecipientsAction
}