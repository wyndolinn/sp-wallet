package com.wynndie.spwallet.sharedFeature.transfer.presentation.transferBetweenCards

import com.wynndie.spwallet.sharedCore.domain.constants.CoreConstants
import com.wynndie.spwallet.sharedCore.domain.constants.emptyAuthedUser
import com.wynndie.spwallet.sharedCore.domain.models.AuthedUser
import com.wynndie.spwallet.sharedCore.domain.models.cards.AuthedCard
import com.wynndie.spwallet.sharedCore.presentation.formatters.input.InputFieldState
import com.wynndie.spwallet.sharedCore.presentation.formatters.LoadingState
import com.wynndie.spwallet.sharedFeature.transfer.domain.models.TransferCard

data class TransferBetweenCardsState(
    val loadingState: LoadingState = LoadingState.Finished,

    val isTransferButtonEnabled: Boolean = false,

    val user: AuthedUser = emptyAuthedUser,
    val sourceCards: List<AuthedCard> = emptyList(),
    val destinationCards: List<TransferCard> = emptyList(),

    val selectedSourceCard: Int = 0,
    val selectedDestinationCard: Int = 0,

    val amountInputFieldState: InputFieldState = InputFieldState(
        maxLength = CoreConstants.MAX_BALANCE_LENGTH
    )
)
