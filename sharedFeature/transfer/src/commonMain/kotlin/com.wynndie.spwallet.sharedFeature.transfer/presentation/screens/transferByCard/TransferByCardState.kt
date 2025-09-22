package com.wynndie.spwallet.sharedFeature.transfer.presentation.screens.transferByCard

import com.wynndie.spwallet.sharedCore.domain.constants.CoreConstants
import com.wynndie.spwallet.sharedCore.domain.constants.emptyAuthedUser
import com.wynndie.spwallet.sharedCore.domain.constants.emptyRecipientCard
import com.wynndie.spwallet.sharedCore.domain.models.AuthedUser
import com.wynndie.spwallet.sharedCore.presentation.models.InputField
import com.wynndie.spwallet.sharedCore.presentation.models.cards.AuthedCardUi
import com.wynndie.spwallet.sharedCore.presentation.models.cards.RecipientCardUi
import com.wynndie.spwallet.sharedCore.presentation.states.LoadingState

data class TransferByCardState(
    val loadingState: LoadingState = LoadingState.Finished,

    val user: AuthedUser = emptyAuthedUser,
    val recipient: RecipientCardUi = RecipientCardUi.of(emptyRecipientCard),

    val cards: List<AuthedCardUi> = emptyList(),

    val carouselPage: Int = 0,
    val isCalculatorSheetVisible: Boolean = false,

    val amountInputField: InputField = InputField(
        maxLength = CoreConstants.MAX_BALANCE_LENGTH
    ),
    val commentInputField: InputField = InputField(
        maxLength = CoreConstants.MAX_COMMENT_LENGTH
    )
)
