package com.wynndie.spwallet.sharedFeature.home.presentation.screen.home

import androidx.compose.ui.text.input.TextFieldValue
import com.wynndie.spwallet.sharedFeature.home.presentation.model.UiAuthedCard
import com.wynndie.spwallet.sharedFeature.home.presentation.model.UiCashCard
import com.wynndie.spwallet.sharedFeature.home.presentation.model.UiUnauthedCard

sealed interface HomeAction {
    data object OnRefresh : HomeAction

    data class OnSwipeCarousel(val index: Int) : HomeAction

    data object OnToggleAuthCardSheet : HomeAction
    data object OnToggleAuthedCardSheet : HomeAction
    data object OnToggleDeleteCardDialog : HomeAction

    data class OnClickAuthCard(val id: String, val token: String) : HomeAction
    data class OnClickDeactivateCard(val card: UiAuthedCard) : HomeAction
    data class OnClickTransferByCard(
        val card: UiAuthedCard,
        val navigate: (String) -> Unit
    ) : HomeAction

    data class OnClickAuthedCard(val card: UiAuthedCard) : HomeAction
    data class OnClickUnauthedCard(val card: UiUnauthedCard) : HomeAction
    data class OnClickCashCard(
        val card: UiCashCard,
        val navigate: (String) -> Unit
    ) : HomeAction

    data class OnChangeCardIdValue(val value: TextFieldValue) : HomeAction
    data class OnChangeCardTokenValue(val value: TextFieldValue) : HomeAction
}