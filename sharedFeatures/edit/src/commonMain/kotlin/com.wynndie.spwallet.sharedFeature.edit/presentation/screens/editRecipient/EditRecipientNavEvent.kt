package com.wynndie.spwallet.sharedFeature.edit.presentation.screens.editRecipient

import com.wynndie.spwallet.sharedCore.presentation.controllers.navigation.NavEvent

interface EditRecipientNavEvent : NavEvent {
    data object OnClickRecipient : EditRecipientNavEvent
}