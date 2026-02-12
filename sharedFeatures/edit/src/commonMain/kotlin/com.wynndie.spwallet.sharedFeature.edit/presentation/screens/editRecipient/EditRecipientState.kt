package com.wynndie.spwallet.sharedFeature.edit.presentation.screens.editRecipient

import com.wynndie.spwallet.sharedCore.domain.constants.CoreConstants
import com.wynndie.spwallet.sharedCore.domain.constants.emptyRecipientCard
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardColors
import com.wynndie.spwallet.sharedCore.presentation.models.cards.RecipientCardUi
import com.wynndie.spwallet.sharedCore.presentation.states.InputFieldState
import com.wynndie.spwallet.sharedCore.presentation.states.LoadingState

data class EditRecipientState(
    val screenLoadingState: LoadingState = LoadingState.Finished,
    val saveLoadingState: LoadingState = LoadingState.Finished,

    val recipient: RecipientCardUi = RecipientCardUi.of(emptyRecipientCard),

    val isCustomizationSheetVisible: Boolean = false,
    val isCalculatorSheetVisible: Boolean = false,
    val isDeleteDialogVisible: Boolean = false,

    val nameInputFieldState: InputFieldState = InputFieldState(
        maxLength = CoreConstants.MAX_CARD_NAME_LENGTH
    ),
    val numberInputFieldState: InputFieldState = InputFieldState(
        maxLength = CoreConstants.CARD_NUMBER_LENGTH
    ),
    val selectedColorChip: CardColors = CardColors.BLUE
)
