package com.wynndie.spwallet.sharedFeature.edit.presentation.screens.customCard

import com.wynndie.spwallet.sharedCore.presentation.controllers.navigation.NavEvent

sealed interface CustomCardNavEvent : NavEvent {

    data object OnClickBack : CustomCardNavEvent
}