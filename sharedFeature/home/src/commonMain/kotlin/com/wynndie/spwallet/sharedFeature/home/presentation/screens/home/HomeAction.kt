package com.wynndie.spwallet.sharedFeature.home.presentation.screens.home

import androidx.compose.ui.text.input.TextFieldValue
import com.wynndie.spwallet.sharedCore.presentation.models.cards.AuthedCardUi

sealed interface HomeAction {
    data object OnRefresh : HomeAction

    data class OnSwipeCarousel(val index: Int) : HomeAction

    data object OnToggleAuthCardSheet : HomeAction
    data object OnToggleAuthedCardSheet : HomeAction
    data object OnToggleDeleteCardDialog : HomeAction

    data class OnClickAuthCard(val id: String, val token: String) : HomeAction
    data class OnClickDeactivateCard(val card: AuthedCardUi) : HomeAction
    data class OnClickTransferByCard(val cardId: String?) : HomeAction

    data class OnClickAuthedCard(val cardId: String) : HomeAction
    data class OnClickUnauthedCard(val cardId: String) : HomeAction
    data class OnClickCustomCard(val cardId: String?) : HomeAction

    data class OnChangeCardIdValue(val value: TextFieldValue) : HomeAction
    data class OnChangeCardTokenValue(val value: TextFieldValue) : HomeAction
}