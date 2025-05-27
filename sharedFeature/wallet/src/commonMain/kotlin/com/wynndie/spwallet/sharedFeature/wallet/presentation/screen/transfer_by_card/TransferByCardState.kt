package com.wynndie.spwallet.sharedFeature.wallet.presentation.screen.transfer_by_card

import com.wynndie.spwallet.sharedCore.presentation.model.InputField
import com.wynndie.spwallet.sharedCore.presentation.model.LoadingState
import com.wynndie.spwallet.sharedFeature.wallet.domain.model.AuthedUser
import com.wynndie.spwallet.sharedFeature.wallet.presentation.model.UiAuthedCard
import com.wynndie.spwallet.sharedFeature.wallet.presentation.model.UiRecipientCard
import com.wynndie.spwallet.sharedFeature.wallet.presentation.model.emptyRecipientCard

data class TransferByCardState(
    val loadingState: LoadingState = LoadingState.Finished,

    val user: AuthedUser = AuthedUser(
        id = "",
        name = ""
    ),

    val recipient: UiRecipientCard = emptyRecipientCard,

    val cards: List<UiAuthedCard> = emptyList(),

    val carouselPage: Int = 0,
    val isCalculatorSheetVisible: Boolean = false,
    val isChangeRecipientSheetVisible: Boolean = false,

    val recipientInputField: InputField = InputField(),
    val amountInputField: InputField = InputField(),
    val commentInputField: InputField = InputField()
)
