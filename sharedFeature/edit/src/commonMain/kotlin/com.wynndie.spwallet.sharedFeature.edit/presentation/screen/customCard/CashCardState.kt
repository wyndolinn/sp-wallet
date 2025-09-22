package com.wynndie.spwallet.sharedFeature.edit.presentation.screen.customCard

import com.wynndie.spwallet.sharedCore.presentation.model.input.InputFieldState
import com.wynndie.spwallet.sharedCore.presentation.model.LoadingState
import com.wynndie.spwallet.sharedCore.domain.constants.Constants
import com.wynndie.spwallet.sharedCore.presentation.model.card.UiCashCard
import com.wynndie.spwallet.sharedCore.presentation.model.emptyUiCashCard

data class CashCardState(
    val screenLoadingState: LoadingState = LoadingState.Finished,
    val saveLoadingState: LoadingState = LoadingState.Finished,

    val card: UiCashCard = emptyUiCashCard,

    val isCustomizationSheetVisible: Boolean = false,
    val isCalculatorSheetVisible: Boolean = false,
    val isDeleteDialogVisible: Boolean = false,

    val nameInputFieldState: InputFieldState = InputFieldState(
        maxLength = Constants.MAX_CARD_NAME_LENGTH
    ),
    val balanceInputFieldState: InputFieldState = InputFieldState(
        maxLength = Constants.MAX_BALANCE_LENGTH
    ),
    val selectedColorChip: Int = 0
)
