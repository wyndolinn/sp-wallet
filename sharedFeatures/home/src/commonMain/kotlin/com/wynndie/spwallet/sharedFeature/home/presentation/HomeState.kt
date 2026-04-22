package com.wynndie.spwallet.sharedFeature.home.presentation

import com.wynndie.spwallet.sharedCore.domain.constants.emptyAuthedUser
import com.wynndie.spwallet.sharedCore.domain.models.AuthedUser
import com.wynndie.spwallet.sharedCore.domain.models.SpServers
import com.wynndie.spwallet.sharedCore.domain.models.cards.AuthedCard
import com.wynndie.spwallet.sharedCore.domain.models.cards.CustomCard
import com.wynndie.spwallet.sharedCore.domain.models.cards.UnauthedCard
import com.wynndie.spwallet.sharedCore.presentation.formatters.LoadingState
import com.wynndie.spwallet.sharedCore.presentation.formatters.InputFieldState

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
    val totalBalance: Long = 0,

    val areCustomCardsVisible: Boolean = true,
    val customCards: List<CustomCard> = emptyList(),
    val authedCards: List<AuthedCard> = emptyList(),
    val unauthedCards: List<UnauthedCard> = emptyList(),

    val idInputFieldState: InputFieldState = InputFieldState(
        maxLength = 36
    ),
    val tokenInputFieldState: InputFieldState = InputFieldState(
        maxLength = 32
    )
)

