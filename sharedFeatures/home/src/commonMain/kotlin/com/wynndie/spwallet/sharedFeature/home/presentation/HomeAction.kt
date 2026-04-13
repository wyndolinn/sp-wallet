package com.wynndie.spwallet.sharedFeature.home.presentation

import androidx.compose.ui.text.input.TextFieldValue
import com.wynndie.spwallet.sharedCore.domain.models.SpServers
import com.wynndie.spwallet.sharedCore.domain.models.cards.AuthedCard

sealed interface HomeAction {
    data object OnRefresh : HomeAction

    data class OnClickServerOption(val server: SpServers) : HomeAction

    data class OnToggleAuthCardSheet(val isOpen: Boolean) : HomeAction
    data class OnToggleAuthedCardSheet(val isOpen: Boolean) : HomeAction
    data class OnToggleDeleteCardDialog(val isOpen: Boolean) : HomeAction

    data class OnClickTransferBetweenCard(val cardId: String?) : HomeAction
    data class OnClickAuthCard(val id: String, val token: String) : HomeAction
    data class OnClickDeactivateCard(val card: AuthedCard) : HomeAction
    data class OnClickTransferByCard(val cardId: String?) : HomeAction

    data class OnClickAuthedCard(val cardId: String) : HomeAction
    data class OnClickUnauthedCard(val cardId: String) : HomeAction
    data class OnClickCustomCard(val cardId: String?) : HomeAction

    data class OnChangeCardIdValue(val value: TextFieldValue) : HomeAction
    data class OnChangeCardTokenValue(val value: TextFieldValue) : HomeAction
    data object OnToggleCardIdFocus : HomeAction
    data object OnToggleCardTokenFocus : HomeAction
}