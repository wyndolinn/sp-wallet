package com.wynndie.spwallet.sharedFeature.transfer.presentation.screens.searchRecipient

import com.wynndie.spwallet.sharedCore.domain.constants.CoreConstants
import com.wynndie.spwallet.sharedCore.presentation.models.cards.RecipientCardUi
import com.wynndie.spwallet.sharedCore.presentation.states.InputFieldState

data class SearchRecipientState(
    val recipientInputFieldState: InputFieldState  = InputFieldState(
        maxLength = 16
    ),
    val recipients: List<RecipientCardUi> = emptyList(),
    val isNewRecipient: Boolean = false
)
