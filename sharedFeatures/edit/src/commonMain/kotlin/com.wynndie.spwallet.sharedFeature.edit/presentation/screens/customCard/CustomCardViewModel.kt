package com.wynndie.spwallet.sharedFeature.edit.presentation.screens.customCard

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wynndie.spwallet.sharedCore.domain.constants.emptyCustomCard
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardColors
import com.wynndie.spwallet.sharedCore.domain.repositories.CardsRepository
import com.wynndie.spwallet.sharedCore.domain.repositories.PreferencesRepository
import com.wynndie.spwallet.sharedCore.domain.validators.BalanceValidator
import com.wynndie.spwallet.sharedCore.domain.validators.CardNameValidator
import com.wynndie.spwallet.sharedCore.domain.validators.models.BalanceValidationValues
import com.wynndie.spwallet.sharedCore.presentation.controllers.navigation.NavController
import com.wynndie.spwallet.sharedCore.presentation.controllers.overlay.OverlayController
import com.wynndie.spwallet.sharedCore.presentation.controllers.overlay.OverlayType.Snackbar
import com.wynndie.spwallet.sharedCore.presentation.extensions.observeInputField
import com.wynndie.spwallet.sharedCore.presentation.extensions.observeValidationStates
import com.wynndie.spwallet.sharedCore.presentation.extensions.validateInputField
import com.wynndie.spwallet.sharedCore.presentation.formatters.UiText.ResourceString
import com.wynndie.spwallet.sharedCore.presentation.formatters.input.InputFilterOptions
import com.wynndie.spwallet.sharedCore.presentation.formatters.input.cutOffAt
import com.wynndie.spwallet.sharedCore.presentation.formatters.input.filterBy
import com.wynndie.spwallet.sharedCore.presentation.states.LoadingState.Finished
import com.wynndie.spwallet.sharedCore.presentation.states.LoadingState.Loading
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.cash_creation_succeed
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CustomCardViewModel(
    private val args: CustomCardParams,
    private val cardsRepository: CardsRepository,
    private val preferencesRepository: PreferencesRepository,
    private val cardNameValidator: CardNameValidator,
    private val balanceValidator: BalanceValidator
) : ViewModel() {

    private val _state = MutableStateFlow(CustomCardState())
    val state = _state.asStateFlow()

    init {
        loadCustomCard()

        observeValidationStates(
            _state.observeInputField(
                inputField = { it.nameInputFieldState },
                validation = { cardNameValidator.validate(it) },
                updateState = { value -> _state.update { it.copy(nameInputFieldState = value) } }
            ),
            _state.observeInputField(
                inputField = { it.balanceInputFieldState },
                validation = {
                    balanceValidator.validate(
                        BalanceValidationValues(
                            it,
                            minValue = 0
                        )
                    )
                },
                updateState = { value -> _state.update { it.copy(balanceInputFieldState = value) } }
            )
        ).onEach { isAllValid ->
            _state.update { it.copy(isSaveButtonEnabled = isAllValid) }
        }.launchIn(viewModelScope)
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
                    .filterBy(InputFilterOptions.LettersOrDigits.predicate)
                    .cutOffAt(_state.value.nameInputFieldState.maxLength) ?: return

                _state.update { state ->
                    state.copy(
                        card = state.card.copy(name = value.text),
                        nameInputFieldState = state.nameInputFieldState.copy(value = value)
                    )
                }
            }

            is CustomCardAction.OnChangeBalanceValue -> {
                val value = action.value
                    .filterBy(InputFilterOptions.DigitsOnly.predicate)
                    .cutOffAt(_state.value.balanceInputFieldState.maxLength) ?: return

                _state.update { state ->
                    state.copy(
                        card = state.card.copy(
                            balance = value.text.ifBlank { "0" }.toLong()
                        ),
                        balanceInputFieldState = state.balanceInputFieldState.copy(
                            value = value
                        )
                    )
                }
            }


            is CustomCardAction.OnClickColorChip -> {
                _state.update { state ->
                    state.copy(
                        card = state.card.copy(color = CardColors.of(action.value)),
                        selectedColorChip = CardColors.of(action.value)
                    )
                }
            }


            is CustomCardAction.OnClickSaveCard -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(saveLoadingState = Loading)
                    }

                    cardsRepository.insertCustomCard(state.value.card)

                    _state.update { it.copy(saveLoadingState = Finished) }
                    OverlayController.send(
                        Snackbar(ResourceString(Res.string.cash_creation_succeed))
                    )
                    NavController.navigate(CustomCardNavEvent.OnClickBack)
                }
            }

            is CustomCardAction.OnClickDeleteCard -> {
                viewModelScope.launch {
                    _state.update { it.copy(isDeleteDialogVisible = false) }
                    cardsRepository.deleteCustomCard(_state.value.card)
                    NavController.navigate(CustomCardNavEvent.OnClickBack)
                }
            }

            CustomCardAction.OnToggleNameFocus -> {
                _state.validateInputField(
                    inputField = { it.nameInputFieldState },
                    validation = { cardNameValidator.validate(it) },
                    updateState = { _state.update { state -> state.copy(nameInputFieldState = it) } }
                )
            }

            CustomCardAction.OnToggleBalanceFocus -> {
                _state.validateInputField(
                    inputField = { it.balanceInputFieldState },
                    validation = { balanceValidator.validate(BalanceValidationValues(it)) },
                    updateState = { value -> _state.update { it.copy(balanceInputFieldState = value) } }
                )
            }
        }
    }

    private fun loadCustomCard() {
        viewModelScope.launch {
            _state.update {
                it.copy(screenLoadingState = Loading)
            }

            val card = cardsRepository.getCustomCards().first()
                .find { it.id == args.cardId }
                ?: emptyCustomCard.copy(
                    server = preferencesRepository.getSelectedSpServer().first()
                )

            _state.update { state ->
                state.copy(
                    card = card,
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
                it.copy(screenLoadingState = Finished)
            }
        }
    }
}