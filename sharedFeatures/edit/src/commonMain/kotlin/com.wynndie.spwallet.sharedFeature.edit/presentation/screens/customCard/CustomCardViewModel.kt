package com.wynndie.spwallet.sharedFeature.edit.presentation.screens.customCard

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wynndie.spwallet.sharedCore.domain.constants.emptyCustomCard
import com.wynndie.spwallet.sharedCore.domain.error.onError
import com.wynndie.spwallet.sharedCore.domain.error.onSuccess
import com.wynndie.spwallet.sharedCore.domain.repositories.CardsRepository
import com.wynndie.spwallet.sharedCore.domain.validators.BalanceValidator
import com.wynndie.spwallet.sharedCore.domain.validators.CardNameValidator
import com.wynndie.spwallet.sharedCore.presentation.controllers.navigation.NavController
import com.wynndie.spwallet.sharedCore.presentation.controllers.overlay.OverlayController
import com.wynndie.spwallet.sharedCore.presentation.controllers.overlay.OverlayType
import com.wynndie.spwallet.sharedCore.presentation.extensions.asUiText
import com.wynndie.spwallet.sharedCore.presentation.extensions.cutOffAt
import com.wynndie.spwallet.sharedCore.presentation.extensions.dropFirst
import com.wynndie.spwallet.sharedCore.presentation.extensions.filterBy
import com.wynndie.spwallet.sharedCore.presentation.formatters.InputFilterOptions
import com.wynndie.spwallet.sharedCore.presentation.formatters.displayableValue.BlocksDisplayableValue
import com.wynndie.spwallet.sharedCore.presentation.formatters.UiText
import com.wynndie.spwallet.sharedCore.domain.models.CardColor
import com.wynndie.spwallet.sharedCore.presentation.models.cards.CustomCardUi
import com.wynndie.spwallet.sharedCore.presentation.states.LoadingState
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.cash_creation_succeed
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CustomCardViewModel(
    private val args: CustomCardViewModelArgs,
    private val cardsRepository: CardsRepository,
    private val cardNameValidator: CardNameValidator,
    private val balanceValidator: BalanceValidator
) : ViewModel() {

    private val _state = MutableStateFlow(CustomCardState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update {
                it.copy(screenLoadingState = LoadingState.Loading)
            }

            val card = cardsRepository.getCustomCards().first()
                .find { it.id == args.cardId }
                ?: emptyCustomCard

            _state.update { state ->
                state.copy(
                    card = CustomCardUi.of(card),
                    selectedColorChip = card.color,
                    nameInputField = state.nameInputField.copy(
                        value = TextFieldValue(card.name)
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

    fun onAction(action: CustomCardAction) {
        when (action) {

            CustomCardAction.OnToggleCalculatorSheet -> {
                _state.update {
                    it.copy(isCalculatorSheetVisible = !state.value.isCalculatorSheetVisible)
                }
            }

            CustomCardAction.OnToggleCustomizationSheet -> {
                _state.update {
                    it.copy(isCustomizationSheetVisible = !state.value.isCustomizationSheetVisible)
                }
            }

            CustomCardAction.OnToggleDeleteDialog -> {
                _state.update {
                    it.copy(isDeleteDialogVisible = !state.value.isDeleteDialogVisible)
                }
            }


            CustomCardAction.OnClickBack -> {
                viewModelScope.launch {
                    NavController.navigate(CustomCardNavEvent.OnClickBack)
                }
            }


            is CustomCardAction.OnChangeNameValue -> {
                val value = action.value
                    .filterBy(InputFilterOptions.Text.LettersOrDigits.predicate)
                    .cutOffAt(_state.value.nameInputField.maxLength) ?: return

                _state.update { state ->
                    state.copy(
                        card = state.card.copy(name = value.text),
                        nameInputField = state.nameInputField.copy(value = value)
                    )
                }
            }

            is CustomCardAction.OnChangeBalanceValue -> {
                val value = action.value
                    .filterBy(InputFilterOptions.Digits.DigitsOnly.predicate)
                    .dropFirst('0')
                    .cutOffAt(_state.value.balanceInputField.maxLength) ?: return

                _state.update { state ->
                    state.copy(
                        card = state.card.copy(
                            balance = BlocksDisplayableValue.of(value.text.ifBlank { "0" }.toLong())
                        ),
                        balanceInputField = state.balanceInputField.copy(
                            value = value
                        )
                    )
                }
            }


            is CustomCardAction.OnClickColorChip -> {
                _state.update { state ->
                    state.copy(
                        card = state.card.copy(iconBackground = CardColor.from(action.value)),
                        selectedColorChip = action.value
                    )
                }
            }


            is CustomCardAction.OnClickSaveCard -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(saveLoadingState = LoadingState.Loading)
                    }

                    val validationResults = listOf(
                        isCardNameValid(action.cardName),
                        isCardBalanceValid(action.cardBalance)
                    )

                    if (!validationResults.any { isValid -> !isValid }) {
                        cardsRepository.insertCustomCard(state.value.card.toDomain())
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
                                OverlayController.send(
                                    OverlayType.Snackbar(UiText.ResourceString(Res.string.cash_creation_succeed))
                                )
                                NavController.navigate(CustomCardNavEvent.OnClickBack)
                            }
                    }
                }
            }

            is CustomCardAction.OnClickDeleteCard -> {
                viewModelScope.launch {
                    _state.update { it.copy(isDeleteDialogVisible = false) }
                    cardsRepository.deleteCustomCard(_state.value.card.toDomain())
                    NavController.navigate(CustomCardNavEvent.OnClickBack)
                }
            }
        }
    }

    private fun isCardNameValid(value: String): Boolean {
        val (isValid, error) = cardNameValidator.validate(value)
        _state.update { state ->
            state.copy(
                nameInputField = state.nameInputField.copy(
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
                balanceInputField = state.balanceInputField.copy(
                    supportingText = error?.asUiText(),
                    hasError = !isValid
                )
            )
        }
        return isValid
    }
}