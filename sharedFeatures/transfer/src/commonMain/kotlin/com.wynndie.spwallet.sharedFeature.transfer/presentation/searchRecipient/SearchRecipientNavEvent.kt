package com.wynndie.spwallet.sharedFeature.transfer.presentation.searchRecipient

import com.wynndie.spwallet.sharedCore.presentation.controllers.navigation.NavEvent

sealed interface SearchRecipientNavEvent : NavEvent {
    data object NavigateBack : SearchRecipientNavEvent
    data class NavigateToTransfer(val cardNumber: String) : SearchRecipientNavEvent
}