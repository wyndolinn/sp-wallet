package com.wynndie.spwallet.sharedFeature.home.presentation

import com.wynndie.spwallet.sharedCore.presentation.controllers.navigation.NavEvent

sealed interface HomeNavEvent : NavEvent {
    data class OnClickTransferBetweenCards(val cardId: String?) : HomeNavEvent
    data class OnClickCustomCard(val cardId: String?) : HomeNavEvent
    data class OnClickTransferByCard(val cardId: String?) : HomeNavEvent
}