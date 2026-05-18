package com.wynndie.spwallet.sharedFeature.home.presentation.screens.auth

import com.wynndie.spwallet.sharedCore.presentation.controllers.navigation.NavEvent

sealed interface AuthNavEvent : NavEvent {
    data object NavigateBack : AuthNavEvent
}