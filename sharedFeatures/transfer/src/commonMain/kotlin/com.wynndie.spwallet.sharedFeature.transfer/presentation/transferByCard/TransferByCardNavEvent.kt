package com.wynndie.spwallet.sharedFeature.transfer.presentation.transferByCard

import com.wynndie.spwallet.sharedCore.presentation.controllers.navigation.NavEvent

sealed interface TransferByCardNavEvent : NavEvent {
    data object NavigateBack : TransferByCardNavEvent
    data object NavigateToEditRecipient : TransferByCardNavEvent
    data object NavigateToResult : TransferByCardNavEvent
}