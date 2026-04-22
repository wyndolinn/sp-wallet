package com.wynndie.spwallet.sharedFeature.transfer.presentation.searchRecipient

import com.wynndie.spwallet.sharedCore.domain.models.cards.RecipientCard
import com.wynndie.spwallet.sharedCore.presentation.formatters.InputFieldState

data class SearchRecipientState(
    val recipientInputFieldState: InputFieldState = InputFieldState(
        maxLength = 24
    ),
    val recipients: List<RecipientCard> = emptyList(),
    val isNewRecipient: Boolean = false
)
