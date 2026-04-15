package com.wynndie.spwallet.sharedFeature.home.presentation

import com.wynndie.spwallet.sharedCore.presentation.controllers.navigation.NavEvent

sealed interface HomeNavEvent : NavEvent {
    data class NavigateToTransferBetweenCards(val cardId: String) : HomeNavEvent
    data class NavigateToCustomCard(val cardId: String) : HomeNavEvent
    data class NavigateToTransferByCard(val cardId: String) : HomeNavEvent
}