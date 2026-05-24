package com.wynndie.spwallet.sharedFeature.edit.presentation.screens.recipients

import com.wynndie.spwallet.sharedCore.domain.models.cards.RecipientCard
import com.wynndie.spwallet.sharedCore.presentation.formatters.InputFieldState
import com.wynndie.spwallet.sharedCore.presentation.formatters.LoadingState

data class RecipientsState(
    val loadingState: LoadingState = LoadingState.Finished,

    val recipients: List<RecipientCard> = emptyList(),
    val selectedRecipient: RecipientCard? = null,
    val recipientInputFieldState: InputFieldState = InputFieldState(
        maxLength = 24
    ),

    val cardNameInputFieldState: InputFieldState = InputFieldState(
        minLength = 1,
        maxLength = 24
    ),
    val cardNumberInputFieldState: InputFieldState = InputFieldState(
        minLength = 5,
        maxLength = 5
    ),

    val isEditRecipientSheetOpen: Boolean = false,
    val isDeleteDialogOpen: Boolean = false
)
