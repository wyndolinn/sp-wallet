package com.wynndie.spwallet.sharedFeature.transfer.presentation.transferByCard

import com.wynndie.spwallet.sharedCore.domain.constants.CoreConstants
import com.wynndie.spwallet.sharedCore.domain.constants.emptyAuthedUser
import com.wynndie.spwallet.sharedCore.domain.constants.emptyRecipientCard
import com.wynndie.spwallet.sharedCore.domain.models.AuthedUser
import com.wynndie.spwallet.sharedCore.domain.models.cards.AuthedCard
import com.wynndie.spwallet.sharedCore.domain.models.cards.RecipientCard
import com.wynndie.spwallet.sharedCore.presentation.formatters.input.InputFieldState
import com.wynndie.spwallet.sharedCore.presentation.formatters.LoadingState

data class TransferByCardState(
    val loadingState: LoadingState = LoadingState.Finished,

    val isTransferButtonEnabled: Boolean = false,
    val user: AuthedUser = emptyAuthedUser,
    val recipient: RecipientCard = emptyRecipientCard,
    val sourceCards: List<AuthedCard> = emptyList(),
    val selectedSourceCard: Int = 0,
    val amountInputFieldState: InputFieldState = InputFieldState(
        maxLength = CoreConstants.MAX_BALANCE_LENGTH
    ),
    val commentInputFieldState: InputFieldState = InputFieldState(
        maxLength = CoreConstants.MAX_COMMENT_LENGTH
    )
)
