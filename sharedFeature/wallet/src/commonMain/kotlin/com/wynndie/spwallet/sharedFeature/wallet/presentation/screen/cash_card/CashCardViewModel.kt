package com.wynndie.spwallet.sharedFeature.wallet.presentation.screen.cash_card

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wynndie.spwallet.sharedCore.domain.error.getOrNull
import com.wynndie.spwallet.sharedCore.domain.error.onError
import com.wynndie.spwallet.sharedCore.domain.error.onSuccess
import com.wynndie.spwallet.sharedCore.presentation.controller.dialog.Dialog
import com.wynndie.spwallet.sharedCore.presentation.controller.dialog.DialogController
import com.wynndie.spwallet.sharedCore.presentation.formatter.DigitsFilter
import com.wynndie.spwallet.sharedCore.presentation.formatter.InputFormatter
import com.wynndie.spwallet.sharedCore.presentation.formatter.TextFilter
import com.wynndie.spwallet.sharedCore.presentation.model.LoadingState
import com.wynndie.spwallet.sharedCore.presentation.text.UiText
import com.wynndie.spwallet.sharedCore.presentation.text.asUiText
import com.wynndie.spwallet.sharedFeature.wallet.domain.constants.Constants
import com.wynndie.spwallet.sharedFeature.wallet.domain.repository.WalletRepository
import com.wynndie.spwallet.sharedFeature.wallet.domain.validator.BalanceValidator
import com.wynndie.spwallet.sharedFeature.wallet.domain.validator.CardNameValidator
import com.wynndie.spwallet.sharedFeature.wallet.presentation.model.BlocksDisplayableValue
import com.wynndie.spwallet.sharedFeature.wallet.presentation.model.CardColor
import com.wynndie.spwallet.sharedFeature.wallet.presentation.model.UiCashCard
import com.wynndie.spwallet.sharedFeature.wallet.presentation.model.emptyCashCard
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.cash_creation_succeed
import com.wynndie.spwallet.sharedResources.total_of_ore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CashCardViewModel(
    args: CashCardViewModelArgs,
    private val walletRepository: WalletRepository,
    private val cardNameValidator: CardNameValidator,
    private val balanceValidator: BalanceValidator
) : ViewModel() {

    private val _state = MutableStateFlow(CashCardState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update {
                it.copy(screenLoadingState = LoadingState.Loading)
            }

            val card = walletRepository.getCashCards().first()
                .find { it.id == args.cardId }
                ?: emptyCashCard.toDomain()

            _state.update { state ->
                state.copy(
                    card = UiCashCard.of(card),
                    selectedColorChip = card.color,
                    nameInputField = state.nameInputField.copy(
                        value = TextFieldValue(card.name),
                        suffix = UiText.DynamicString("")
                    ),
                    balanceInputField = state.balanceInputField.copy(
                        value = TextFieldValue(card.balance.toString())
                    )
                )
            }

            _state.update {
                it.copy(screenLoadingState = LoadingState.Finished)
            }
        }
    }

    fun onAction(action: CashCardAction) {
        when (action) {

            CashCardAction.OnToggleCalculatorSheet -> {
                _state.update {
                    it.copy(isCalculatorSheetVisible = !state.value.isCalculatorSheetVisible)
                }
            }

            CashCardAction.OnToggleCustomizationSheet -> {
                _state.update {
                    it.copy(isCustomizationSheetVisible = !state.value.isCustomizationSheetVisible)
                }
            }

            CashCardAction.OnToggleDeleteDialog -> {
                _state.update {
                    it.copy(isDeleteDialogVisible = !state.value.isDeleteDialogVisible)
                }
            }


            is CashCardAction.OnChangeNameValue -> {
                val inputFormatter = InputFormatter(action.value)
                    .filterBy(TextFilter.LETTERS_OR_DIGITS.predicate)
                    .cutOffAt(Constants.MAX_CARD_NAME_LENGTH) ?: return

                _state.update { state ->
                    state.copy(
                        card = state.card.copy(
                            name = inputFormatter.value.text
                        ),
                        nameInputField = state.nameInputField.copy(
                            value = inputFormatter.value
                        )
                    )
                }
            }

            is CashCardAction.OnChangeBalanceValue -> {
                val inputFormatter = InputFormatter(action.value)
                    .filterBy(DigitsFilter.DIGITS_ONLY.predicate)
                    .dropFirst("0")
                    .cutOffAt(Constants.MAX_BALANCE_LENGTH) ?: return

                val balance = if (inputFormatter.value.text == "") {
                    BlocksDisplayableValue.of(0L)
                } else {
                    BlocksDisplayableValue.of(inputFormatter.value.text.toLong())
                }

                _state.update { state ->
                    state.copy(
                        card = state.card.copy(
                            title = UiText.StringResourceId(Res.string.total_of_ore, balance.value),
                            balance = balance
                        ),
                        balanceInputField = state.balanceInputField.copy(
                            value = inputFormatter.value
                        )
                    )
                }
            }


            is CashCardAction.OnClickColorChip -> {
                _state.update { state ->
                    state.copy(
                        card = state.card.copy(iconBackground = CardColor.from(action.value)),
                        selectedColorChip = action.value
                    )
                }
            }


            is CashCardAction.OnClickSaveCard -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(saveLoadingState = LoadingState.Loading)
                    }

                    val validationResults = listOf(
                        isCardNameValid(action.cardName),
                        isCardBalanceValid(action.cardBalance)
                    )

                    if (!validationResults.any { isValid -> !isValid }) {
                        walletRepository.insertCashCard(state.value.card.toDomain())
                            .onError { error ->
                                _state.update {
                                    it.copy(
                                        screenLoadingState = LoadingState.Failed(error.asUiText()),
                                        saveLoadingState = LoadingState.Failed(error.asUiText())
                                    )
                                }
                            }
                            .onSuccess {
                                _state.update { it.copy(saveLoadingState = LoadingState.Finished) }
                                DialogController.send(
                                    Dialog.Toast(UiText.StringResourceId(Res.string.cash_creation_succeed))
                                )
                                action.navigateBack()
                            }
                    }
                }
            }

            is CashCardAction.OnClickDeleteCard -> {
                viewModelScope.launch {
                    _state.update { it.copy(isDeleteDialogVisible = false) }
                    walletRepository.deleteCashCard(_state.value.card.toDomain())
                    action.navigateBack()
                }
            }
        }
    }

    private fun isCardNameValid(value: String): Boolean {
        return cardNameValidator.validate(value)
            .onError {
                _state.update { state ->
                    state.copy(
                        nameInputField = state.nameInputField.copy(
                            supportingText = UiText.DynamicString(""),
                            isError = false
                        )
                    )
                }
            }
            .onSuccess {
                _state.update { state ->
                    state.copy(
                        nameInputField = state.nameInputField.copy(
                            supportingText = UiText.DynamicString(""),
                            isError = false
                        )
                    )
                }
            }
            .getOrNull() ?: false
    }

    private fun isCardBalanceValid(value: String): Boolean {
        return balanceValidator.validate(value)
            .onError {
                _state.update { state ->
                    state.copy(
                        balanceInputField = state.balanceInputField.copy(
                            supportingText = UiText.DynamicString(""),
                            isError = false
                        )
                    )
                }
            }
            .onSuccess {
                _state.update { state ->
                    state.copy(
                        balanceInputField = state.balanceInputField.copy(
                            supportingText = UiText.DynamicString(""),
                            isError = false
                        )
                    )
                }
            }
            .getOrNull() ?: false
    }
}