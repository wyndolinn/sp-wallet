package com.wynndie.spwallet.sharedFeature.home.presentation.screen.home

import androidx.compose.ui.text.input.TextFieldValue
import com.wynndie.spwallet.sharedCore.presentation.model.card.UiAuthedCard
import com.wynndie.spwallet.sharedCore.presentation.model.card.UiCashCard
import com.wynndie.spwallet.sharedCore.presentation.model.card.UiUnauthedCard

sealed interface HomeAction {
    data object OnRefresh : HomeAction

    data class OnSwipeCarousel(val index: Int) : HomeAction

    data object OnToggleAuthCardSheet : HomeAction
    data object OnToggleAuthedCardSheet : HomeAction
    data object OnToggleDeleteCardDialog : HomeAction

    data object OnClickTopAppBar : HomeAction

    data class OnClickAuthCard(val id: String, val token: String) : HomeAction
    data class OnClickDeactivateCard(val card: UiAuthedCard) : HomeAction
    data class OnClickTransferByCard(val cardId: String?) : HomeAction

    data class OnClickAuthedCard(val cardId: String) : HomeAction
    data class OnClickUnauthedCard(val cardId: String) : HomeAction
    data class OnClickCashCard(val cardId: String?) : HomeAction

    data class OnChangeCardIdValue(val value: TextFieldValue) : HomeAction
    data class OnChangeCardTokenValue(val value: TextFieldValue) : HomeAction
}