package com.wynndie.spwallet.sharedFeature.wallet.presentation.screen.cash_card

import com.wynndie.spwallet.sharedCore.presentation.model.input.InputField
import com.wynndie.spwallet.sharedCore.presentation.model.LoadingState
import com.wynndie.spwallet.sharedFeature.wallet.presentation.model.UiCashCard
import com.wynndie.spwallet.sharedFeature.wallet.presentation.model.emptyCashCard

data class CashCardState(
    val screenLoadingState: LoadingState = LoadingState.Finished,
    val saveLoadingState: LoadingState = LoadingState.Finished,

    val card: UiCashCard = emptyCashCard,

    val isCustomizationSheetVisible: Boolean = false,
    val isCalculatorSheetVisible: Boolean = false,
    val isDeleteDialogVisible: Boolean = false,

    val nameInputField: InputField = InputField(),
    val balanceInputField: InputField = InputField(),
    val selectedColorChip: Int = 0
)
