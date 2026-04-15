package com.wynndie.spwallet.sharedFeature.edit.presentation

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
import com.wynndie.spwallet.sharedCore.presentation.formatters.input.InputFilters
import com.wynndie.spwallet.sharedCore.presentation.formatters.input.cutOffAt
import com.wynndie.spwallet.sharedCore.presentation.formatters.input.filterBy
import com.wynndie.spwallet.sharedCore.presentation.formatters.LoadingState.Finished
import com.wynndie.spwallet.sharedCore.presentation.formatters.LoadingState.Loading
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
            CustomCardAction.NavigateBack -> onBack()
            CustomCardAction.DeleteCard -> deleteCard()
            CustomCardAction.SaveCard -> saveCard()
            is CustomCardAction.SelectColor -> selectColor(action.value)
            is CustomCardAction.ChangeNameValue -> changeNameValue(action.value)
            is CustomCardAction.ChangeBalanceValue -> changeBalanceValue(action.value)
            CustomCardAction.ClearNameFocus -> onClearNameFocus()
            CustomCardAction.ClearBalanceFocus -> onClearBalanceFocus()
            is CustomCardAction.ToggleCustomizationSheet -> toggleCustomizationSheet(action.open)
            is CustomCardAction.ToggleDeleteDialog -> toggleDeleteCardDialog(action.open)
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

    private fun onBack() {
        viewModelScope.launch {
            NavController.navigate(CustomCardNavEvent.NavigateBack)
        }
    }

    private fun deleteCard() {
        viewModelScope.launch {
            _state.update { it.copy(isDeleteDialogVisible = false) }
            cardsRepository.deleteCustomCard(_state.value.card)
            NavController.navigate(CustomCardNavEvent.NavigateBack)
        }
    }

    private fun saveCard() {
        viewModelScope.launch {
            _state.update {
                it.copy(saveLoadingState = Loading)
            }

            cardsRepository.insertCustomCard(state.value.card)

            _state.update { it.copy(saveLoadingState = Finished) }
            OverlayController.send(
                Snackbar(ResourceString(Res.string.cash_creation_succeed))
            )
            NavController.navigate(CustomCardNavEvent.NavigateBack)
        }
    }

    private fun selectColor(color: Int) {
        _state.update { state ->
            state.copy(
                card = state.card.copy(color = CardColors.of(color)),
                selectedColorChip = CardColors.of(color)
            )
        }
    }

    private fun changeNameValue(value: TextFieldValue) {
        val value = value
            .filterBy(InputFilters.LettersOrDigits.predicate)
            .cutOffAt(_state.value.nameInputFieldState.maxLength) ?: return

        _state.update { state ->
            state.copy(
                card = state.card.copy(name = value.text),
                nameInputFieldState = state.nameInputFieldState.copy(value = value)
            )
        }
    }

    private fun changeBalanceValue(value: TextFieldValue) {
        val value = value
            .filterBy(InputFilters.DigitsOnly.predicate)
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

    private fun onClearNameFocus() {
        _state.validateInputField(
            inputField = { it.nameInputFieldState },
            validation = { cardNameValidator.validate(it) },
            updateState = { _state.update { state -> state.copy(nameInputFieldState = it) } }
        )
    }

    private fun onClearBalanceFocus() {
        _state.validateInputField(
            inputField = { it.balanceInputFieldState },
            validation = { balanceValidator.validate(BalanceValidationValues(it)) },
            updateState = { value -> _state.update { it.copy(balanceInputFieldState = value) } }
        )
    }

    private fun toggleCustomizationSheet(open: Boolean) {
        _state.update { it.copy(isCustomizationSheetVisible = open) }
    }

    private fun toggleDeleteCardDialog(open: Boolean) {
        _state.update { it.copy(isDeleteDialogVisible = open) }
    }
}