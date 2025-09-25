package com.wynndie.spwallet.sharedFeature.transfer.presentation.screens.searchRecipient

import com.wynndie.spwallet.sharedCore.domain.constants.CoreConstants
import com.wynndie.spwallet.sharedCore.presentation.models.cards.RecipientCardUi
import com.wynndie.spwallet.sharedCore.presentation.models.InputField

data class SearchRecipientState(
    val recipientInputField: InputField  = InputField(
        maxLength = CoreConstants.MAX_RECIPIENT_NAME_LENGTH
    ),
    val recipients: List<RecipientCardUi> = emptyList(),
    val isNewRecipient: Boolean = false
)
