package com.wynndie.spwallet.sharedFeature.transfer.presentation.transferByCard

import com.wynndie.spwallet.sharedCore.presentation.controllers.navigation.NavEvent

sealed interface TransferByCardNavEvent : NavEvent {
    data object OnClickBack : TransferByCardNavEvent
    data object OnClickRecipient : TransferByCardNavEvent
    data object OnTransferSuccess : TransferByCardNavEvent
}