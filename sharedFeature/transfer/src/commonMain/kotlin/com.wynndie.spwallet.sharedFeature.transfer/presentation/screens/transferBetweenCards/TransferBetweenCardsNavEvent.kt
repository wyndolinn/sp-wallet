package com.wynndie.spwallet.sharedFeature.transfer.presentation.screens.transferBetweenCards

import com.wynndie.spwallet.sharedCore.presentation.controllers.navigation.NavEvent

sealed interface TransferBetweenCardsNavEvent : NavEvent {
    
    data object OnClickBack : TransferBetweenCardsNavEvent
}