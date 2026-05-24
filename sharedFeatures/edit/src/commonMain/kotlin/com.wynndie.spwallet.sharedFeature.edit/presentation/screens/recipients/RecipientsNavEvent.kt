package com.wynndie.spwallet.sharedFeature.edit.presentation.screens.recipients

import com.wynndie.spwallet.sharedCore.presentation.controllers.navigation.NavEvent

sealed interface RecipientsNavEvent : NavEvent {
    data object NavigateBack : RecipientsNavEvent
    data class NavigateToTransfer(val number: String) : RecipientsNavEvent
}