package com.wynndie.spwallet.sharedFeature.transfer.presentation.screen.searchRecipient

import com.wynndie.spwallet.sharedCore.domain.constants.Constants
import com.wynndie.spwallet.sharedCore.presentation.model.UiRecipient
import com.wynndie.spwallet.sharedCore.presentation.model.input.InputFieldState

data class SearchRecipientState(
    val recipientInputFieldState: InputFieldState  = InputFieldState(
        maxLength = Constants.MAX_RECIPIENT_NAME_LENGTH
    ),
    val recipients: List<UiRecipient> = emptyList()
)
