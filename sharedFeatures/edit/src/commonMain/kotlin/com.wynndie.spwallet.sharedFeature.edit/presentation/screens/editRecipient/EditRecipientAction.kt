package com.wynndie.spwallet.sharedFeature.edit.presentation.screens.editRecipient

sealed interface EditRecipientAction {
    data object OnClickBack : EditRecipientAction
}