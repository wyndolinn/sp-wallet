package com.wynndie.spwallet.sharedFeature.transfer.presentation.transferBetweenCards

import com.wynndie.spwallet.sharedCore.presentation.controllers.navigation.NavEvent

sealed interface TransferBetweenCardsNavEvent : NavEvent {
    data object NavigateBack : TransferBetweenCardsNavEvent
    data object NavigateToResult : TransferBetweenCardsNavEvent
}