package com.wynndie.spwallet.sharedFeature.edit.presentation

import com.wynndie.spwallet.sharedCore.domain.constants.CoreConstants
import com.wynndie.spwallet.sharedCore.domain.constants.emptyCustomCard
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardColors
import com.wynndie.spwallet.sharedCore.domain.models.cards.CustomCard
import com.wynndie.spwallet.sharedCore.presentation.formatters.input.InputFieldState
import com.wynndie.spwallet.sharedCore.presentation.formatters.LoadingState

data class CustomCardState(
    val screenLoadingState: LoadingState = LoadingState.Finished,
    val saveLoadingState: LoadingState = LoadingState.Finished,

    val card: CustomCard = emptyCustomCard,
    val isSaveButtonEnabled: Boolean = false,

    val isCustomizationSheetVisible: Boolean = false,
    val isDeleteDialogVisible: Boolean = false,

    val nameInputFieldState: InputFieldState = InputFieldState(
        maxLength = 12
    ),
    val balanceInputFieldState: InputFieldState = InputFieldState(
        maxLength = CoreConstants.MAX_BALANCE_LENGTH
    ),
    val selectedColorChip: CardColors = CardColors.BLUE
)
