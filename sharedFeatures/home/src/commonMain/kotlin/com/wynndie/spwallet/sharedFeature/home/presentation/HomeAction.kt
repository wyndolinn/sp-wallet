package com.wynndie.spwallet.sharedFeature.home.presentation

import androidx.compose.ui.text.input.TextFieldValue
import com.wynndie.spwallet.sharedCore.domain.models.SpServers
import com.wynndie.spwallet.sharedCore.domain.models.cards.AuthedCard

sealed interface HomeAction {
    data object Refresh : HomeAction

    data class SelectServer(val server: SpServers) : HomeAction

    data class ToggleAuthCardSheet(val open: Boolean) : HomeAction
    data class ToggleAuthedCardSheet(val open: Boolean) : HomeAction
    data class ToggleDeleteCardDialog(val open: Boolean) : HomeAction

    data class TransferBetweenCards(val id: String) : HomeAction
    data class TransferByCard(val id: String) : HomeAction
    data class AuthCard(val id: String, val token: String) : HomeAction
    data class DeactivateCard(val card: AuthedCard) : HomeAction

    data class SelectAuthedCard(val id: String) : HomeAction
    data class SelectUnauthedCard(val id: String) : HomeAction
    data class SelectCustomCard(val id: String) : HomeAction

    data class ChangeCardIdValue(val value: TextFieldValue) : HomeAction
    data class ChangeTokenValue(val value: TextFieldValue) : HomeAction

    data object ClearIdFocus : HomeAction
    data object ClearCardTokenFocus : HomeAction
}