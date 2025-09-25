package com.wynndie.spwallet.sharedFeature.home.presentation.screens.home

import com.wynndie.spwallet.sharedCore.presentation.states.LoadingState
import com.wynndie.spwallet.sharedCore.presentation.models.InputField
import com.wynndie.spwallet.sharedCore.domain.constants.CoreConstants
import com.wynndie.spwallet.sharedCore.domain.models.AuthedUser
import com.wynndie.spwallet.sharedCore.presentation.formatters.displayableValue.BlocksDisplayableValue
import com.wynndie.spwallet.sharedCore.presentation.models.cards.AuthedCardUi
import com.wynndie.spwallet.sharedCore.presentation.models.cards.CustomCardUi
import com.wynndie.spwallet.sharedCore.presentation.models.cards.UnauthedCardUi

data class HomeState(
    val screenLoadingState: LoadingState = LoadingState.Finished,
    val authLoadingState: LoadingState = LoadingState.Finished,

    val carouselPage: Int = 0,

    val isAuthCardSheetVisible: Boolean = false,
    val isAuthedCardSheetVisible: Boolean = false,
    val isDeactivateCardDialogVisible: Boolean = false,

    val isAuthButtonEnabled: Boolean = false,

    val authedUser: AuthedUser = AuthedUser("", ""),
    val totalBalance: BlocksDisplayableValue = BlocksDisplayableValue.of(0L),

    val areCustomCardsVisible: Boolean = true,
    val customCards: List<CustomCardUi> = emptyList(),
    val authedCards: List<AuthedCardUi> = emptyList(),
    val unauthedCards: List<UnauthedCardUi> = emptyList(),

    val idInputField: InputField = InputField(
        maxLength = CoreConstants.UUID_LENGTH
    ),
    val tokenInputField: InputField = InputField(
        maxLength = CoreConstants.TOKEN_LENGTH
    )
)

