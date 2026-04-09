package com.wynndie.spwallet.sharedFeature.home.presentation.screens.home

import com.wynndie.spwallet.sharedCore.presentation.states.LoadingState
import com.wynndie.spwallet.sharedCore.presentation.states.InputFieldState
import com.wynndie.spwallet.sharedCore.domain.constants.CoreConstants
import com.wynndie.spwallet.sharedCore.domain.constants.emptyAuthedUser
import com.wynndie.spwallet.sharedCore.domain.models.AuthedUser
import com.wynndie.spwallet.sharedCore.domain.models.SpServers
import com.wynndie.spwallet.sharedCore.presentation.formatters.displayableValue.OreDisplayableValue
import com.wynndie.spwallet.sharedCore.presentation.models.cards.AuthedCardUi
import com.wynndie.spwallet.sharedCore.presentation.models.cards.CustomCardUi
import com.wynndie.spwallet.sharedCore.presentation.models.cards.UnauthedCardUi

data class HomeState(
    val screenLoadingState: LoadingState = LoadingState.Finished,
    val authLoadingState: LoadingState = LoadingState.Finished,

    val carouselPage: Int = 0,
    val selectedServer: SpServers = SpServers.SP,

    val isAuthCardSheetVisible: Boolean = false,
    val isAuthedCardSheetVisible: Boolean = false,
    val isDeactivateCardDialogVisible: Boolean = false,

    val isAuthButtonEnabled: Boolean = false,

    val authedUser: AuthedUser = emptyAuthedUser,
    val totalBalance: OreDisplayableValue = OreDisplayableValue.of(0L),

    val areCustomCardsVisible: Boolean = true,
    val customCards: List<CustomCardUi> = emptyList(),
    val authedCards: List<AuthedCardUi> = emptyList(),
    val unauthedCards: List<UnauthedCardUi> = emptyList(),

    val idInputFieldState: InputFieldState = InputFieldState(
        maxLength = CoreConstants.UUID_LENGTH
    ),
    val tokenInputFieldState: InputFieldState = InputFieldState(
        maxLength = CoreConstants.TOKEN_LENGTH
    )
)

