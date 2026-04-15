package com.wynndie.spwallet.sharedFeature.edit.presentation

import com.wynndie.spwallet.sharedCore.presentation.controllers.navigation.NavEvent

sealed interface CustomCardNavEvent : NavEvent {
    data object NavigateBack : CustomCardNavEvent
}