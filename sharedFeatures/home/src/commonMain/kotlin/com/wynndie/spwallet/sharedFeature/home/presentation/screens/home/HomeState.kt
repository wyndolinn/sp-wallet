package com.wynndie.spwallet.sharedFeature.home.presentation.screens.home

import com.wynndie.spwallet.sharedCore.domain.constants.emptyAuthedUser
import com.wynndie.spwallet.sharedCore.domain.models.AuthedUser
import com.wynndie.spwallet.sharedCore.domain.models.SpServers
import com.wynndie.spwallet.sharedCore.domain.models.cards.AuthedCard
import com.wynndie.spwallet.sharedCore.domain.models.cards.CustomCard
import com.wynndie.spwallet.sharedCore.domain.models.cards.UnauthedCard
import com.wynndie.spwallet.sharedCore.presentation.formatters.DisplayableOreValue
import com.wynndie.spwallet.sharedCore.presentation.formatters.LoadingState

data class HomeState(
    val screenLoadingState: LoadingState = LoadingState.Finished,

    val carouselPage: Int = 0,
    val selectedServer: SpServers = SpServers.SP,

    val isAuthedCardSheetVisible: Boolean = false,
    val isDeactivateCardDialogVisible: Boolean = false,

    val authedUser: AuthedUser = emptyAuthedUser,
    val totalBalance: DisplayableOreValue = DisplayableOreValue.of(0),

    val customCards: List<CustomCard> = emptyList(),
    val authedCards: List<AuthedCard> = emptyList(),
    val unauthedCards: List<UnauthedCard> = emptyList()
)

