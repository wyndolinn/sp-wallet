package com.wynndie.spwallet.sharedFeature.home.presentation.screen.cash_card

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wynndie.spwallet.sharedCore.domain.error.onError
import com.wynndie.spwallet.sharedCore.domain.error.onSuccess
import com.wynndie.spwallet.sharedCore.presentation.controller.dialog.Dialog
import com.wynndie.spwallet.sharedCore.presentation.controller.dialog.DialogController
import com.wynndie.spwallet.sharedCore.presentation.mapper.asUiText
import com.wynndie.spwallet.sharedCore.presentation.model.input.InputFilterOptions
import com.wynndie.spwallet.sharedCore.presentation.model.LoadingState
import com.wynndie.spwallet.sharedCore.presentation.model.UiText
import com.wynndie.spwallet.sharedFeature.home.domain.repository.WalletRepository
import com.wynndie.spwallet.sharedCore.domain.validator.BalanceValidator
import com.wynndie.spwallet.sharedCore.domain.validator.CardNameValidator
import com.wynndie.spwallet.sharedCore.presentation.model.BlocksDisplayableValue
import com.wynndie.spwallet.sharedCore.presentation.model.card.CardColor
import com.wynndie.spwallet.sharedCore.presentation.model.card.UiCashCard
import com.wynndie.spwallet.sharedCore.presentation.model.emptyUiCashCard
import com.wynndie.spwallet.sharedCore.presentation.model.input.cutOffAt
import com.wynndie.spwallet.sharedCore.presentation.model.input.dropFirst
import com.wynndie.spwallet.sharedCore.presentation.model.input.filterBy
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.cash_creation_succeed
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CashCardViewModel(
    private val args: CashCardViewModelArgs,
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
                ?: emptyUiCashCard.toDomain()

            _state.update { state ->
                state.copy(
                    card = UiCashCard.of(card),
                    selectedColorChip = card.color,
                    nameInputFieldState = state.nameInputFieldState.copy(
                        value = TextFieldValue(card.name)
                    ),
                    balanceInputFieldState = state.balanceInputFieldState.copy(
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


            CashCardAction.OnClickBack -> {
                args.onClickBack()
            }


            is CashCardAction.OnChangeNameValue -> {
                val value = action.value
                    .filterBy(InputFilterOptions.Text.LettersOrDigits.predicate)
                    .cutOffAt(_state.value.nameInputFieldState.maxLength) ?: return

                _state.update { state ->
                    state.copy(
                        card = state.card.copy(name = value.text),
                        nameInputFieldState = state.nameInputFieldState.copy(value = value)
                    )
                }
            }

            is CashCardAction.OnChangeBalanceValue -> {
                val value = action.value
                    .filterBy(InputFilterOptions.Digits.DigitsOnly.predicate)
                    .dropFirst("0")
                    .cutOffAt(_state.value.balanceInputFieldState.maxLength) ?: return

                _state.update { state ->
                    state.copy(
                        card = state.card.copy(
                            balance = BlocksDisplayableValue.of(value.text.ifBlank { "0" }.toLong())
                        ),
                        balanceInputFieldState = state.balanceInputFieldState.copy(
                            value = value
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
                                    Dialog.Snackbar(UiText.StringResourceId(Res.string.cash_creation_succeed))
                                )
                                args.onClickBack()
                            }
                    }
                }
            }

            is CashCardAction.OnClickDeleteCard -> {
                viewModelScope.launch {
                    _state.update { it.copy(isDeleteDialogVisible = false) }
                    walletRepository.deleteCashCard(_state.value.card.toDomain())
                    args.onClickBack()
                }
            }
        }
    }

    private fun isCardNameValid(value: String): Boolean {
        val (isValid, error) = cardNameValidator.validate(value)
        _state.update { state ->
            state.copy(
                nameInputFieldState = state.nameInputFieldState.copy(
                    supportingText = error?.asUiText(),
                    hasError = !isValid
                )
            )
        }
        return isValid
    }

    private fun isCardBalanceValid(value: String): Boolean {
        val (isValid, error) = balanceValidator.validate(value)
        _state.update { state ->
            state.copy(
                balanceInputFieldState = state.balanceInputFieldState.copy(
                    supportingText = error?.asUiText(),
                    hasError = !isValid
                )
            )
        }
        return isValid
    }
}