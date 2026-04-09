package com.wynndie.spwallet.sharedFeature.transfer.presentation.screens.transferResult

import com.wynndie.spwallet.sharedCore.presentation.controllers.navigation.NavEvent

sealed interface TransferResultNavEvent : NavEvent {

    data object OnClickComplete : TransferResultNavEvent

    data class OnClickSaveRecipient(
        val cardNumber: String,
        val id: String? = null
    ) : TransferResultNavEvent
}