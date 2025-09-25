package com.wynndie.spwallet.sharedFeature.transfer.presentation.screens.searchRecipient

import com.wynndie.spwallet.sharedCore.presentation.controllers.navigation.NavEvent

sealed interface SearchRecipientNavEvent : NavEvent {

    data object OnClickBack : SearchRecipientNavEvent

    data class OnClickRecipient(
        val id: String?,
        val cardNumber: String
    ) : SearchRecipientNavEvent

    data class OnClickEditRecipient(
        val id: String?
    ) : SearchRecipientNavEvent
}