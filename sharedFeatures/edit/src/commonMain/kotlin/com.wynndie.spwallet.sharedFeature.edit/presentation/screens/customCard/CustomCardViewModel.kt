package com.wynndie.spwallet.sharedFeature.edit.presentation.screens.customCard

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wynndie.spwallet.sharedCore.Res
import com.wynndie.spwallet.sharedCore.cash_creation_succeed
import com.wynndie.spwallet.sharedCore.domain.helpers.emptyCustomCard
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardColors
import com.wynndie.spwallet.sharedCore.domain.repositories.CardsRepository
import com.wynndie.spwallet.sharedCore.domain.repositories.PreferencesRepository
import com.wynndie.spwallet.sharedCore.domain.validators.AmountValidator
import com.wynndie.spwallet.sharedCore.domain.validators.CardNameValidator
import com.wynndie.spwallet.sharedCore.no_name
import com.wynndie.spwallet.sharedCore.presentation.controllers.navigation.NavEventController
import com.wynndie.spwallet.sharedCore.presentation.controllers.overlay.SnackbarController
import com.wynndie.spwallet.sharedCore.presentation.extensions.cutOffAt
import com.wynndie.spwallet.sharedCore.presentation.extensions.dropWhile
import com.wynndie.spwallet.sharedCore.presentation.extensions.filter
import com.wynndie.spwallet.sharedCore.presentation.extensions.observeValidStates
import com.wynndie.spwallet.sharedCore.presentation.extensions.resetValidationState
import com.wynndie.spwallet.sharedCore.presentation.extensions.select
import com.wynndie.spwallet.sharedCore.presentation.extensions.trimSpaces
import com.wynndie.spwallet.sharedCore.presentation.extensions.validateInputField
import com.wynndie.spwallet.sharedCore.presentation.formatters.LoadingState.Finished
import com.wynndie.spwallet.sharedCore.presentation.formatters.LoadingState.Loading
import com.wynndie.spwallet.sharedCore.presentation.models.InputFilters
import com.wynndie.spwallet.sharedCore.presentation.models.UiText.ResourceString
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
    private val amountValidator: AmountValidator,
    private val navEventController: NavEventController,
    private val snackbarController: SnackbarController,
) : ViewModel() {

    private val _state = MutableStateFlow(CustomCardState())
    val state = _state.asStateFlow()


    init {
        loadCustomCard()
        observeInputValidation()
    }


    fun onAction(action: CustomCardAction) {
        when (action) {
            is CustomCardAction.SaveCard -> {
                viewModelScope.launch {
                    _state.update { it.copy(saveLoadingState = Loading) }

                    val card = action.card.copy(
                        name = action.card.name.ifBlank {
                            ResourceString(Res.string.no_name).asStringAsync()
                        }
                    )

                    cardsRepository.insertCustomCard(card)

                    _state.update { it.copy(saveLoadingState = Finished) }
                    snackbarController.send(ResourceString(Res.string.cash_creation_succeed))
                    navEventController.navigate(CustomCardNavEvent.NavigateBack)
                }
            }

            is CustomCardAction.DeleteCard -> {
                viewModelScope.launch {
                    _state.update { it.copy(isDeleteDialogVisible = false) }
                    cardsRepository.deleteCustomCard(action.card)
                    navEventController.navigate(CustomCardNavEvent.NavigateBack)
                }
            }

            is CustomCardAction.SelectColor -> {
                _state.update { state ->
                    state.copy(
                        card = state.card.copy(color = CardColors.of(action.value)),
                        selectedColorChip = CardColors.of(action.value),
                        isCustomizationSheetVisible = false,
                    )
                }
            }

            is CustomCardAction.ChangeNameValue -> {
                _state.update { state ->
                    val value = action.value
                        .filter(InputFilters.Text)
                        .trimSpaces()
                        .cutOffAt(state.nameInputFieldState.maxLength) ?: return@update state

                    state.copy(
                        card = state.card.copy(name = value.text),
                        nameInputFieldState = state.nameInputFieldState.copy(value = value),
                    )
                }
            }

            is CustomCardAction.ChangeBalanceValue -> {
                _state.update { state ->
                    val value = action.value
                        .filter(InputFilters.Numbers)
                        .dropWhile('0') { it.length > 1 }
                        .cutOffAt(state.balanceInputFieldState.maxLength) ?: return@update state

                    state.copy(
                        card = state.card.copy(
                            balance = value.text.ifBlank { "0" }.toLong(),
                        ),
                        balanceInputFieldState = state.balanceInputFieldState.copy(
                            value = value,
                        ),
                    )
                }
            }

            CustomCardAction.ClearNameFocus -> {
                _state.validateInputField(
                    inputField = { it.nameInputFieldState },
                    validator = { cardNameValidator.validate(it) }
                ) { copy(nameInputFieldState = it) }
            }

            CustomCardAction.ClearBalanceFocus -> {
                _state.validateInputField(
                    inputField = { it.balanceInputFieldState },
                    validator = amountValidator::validate
                ) { copy(balanceInputFieldState = it) }
            }

            CustomCardAction.NavigateBack -> {
                viewModelScope.launch {
                    navEventController.navigate(CustomCardNavEvent.NavigateBack)
                }
            }

            is CustomCardAction.ToggleCustomizationSheet -> {
                _state.update { it.copy(isCustomizationSheetVisible = action.open) }
            }

            is CustomCardAction.ToggleDeleteDialog -> {
                _state.update { it.copy(isDeleteDialogVisible = action.open) }
            }
        }
    }


    private fun loadCustomCard() {
        viewModelScope.launch {
            _state.update { it.copy(screenLoadingState = Loading) }

            val card = cardsRepository.getCustomCards().first()
                .find { it.id == args.cardId }
                ?: emptyCustomCard.copy(server = preferencesRepository.getSelectedServer().first())

            _state.update { state ->
                state.copy(
                    card = card,
                    selectedColorChip = card.color,
                    nameInputFieldState = state.nameInputFieldState.copy(
                        value = TextFieldValue(card.name),
                    ),
                    balanceInputFieldState = state.balanceInputFieldState.copy(
                        value = TextFieldValue(card.balance.toString()),
                    ),
                )
            }

            _state.update { it.copy(screenLoadingState = Finished) }
        }
    }

    private fun observeInputValidation() {
        _state
            .select { it.nameInputFieldState.value }
            .onEach {
                _state.resetValidationState(
                    inputField = { it.nameInputFieldState },
                    validator = cardNameValidator::validate
                ) { copy(nameInputFieldState = it) }
            }.launchIn(viewModelScope)

        _state
            .select { it.balanceInputFieldState.value }
            .onEach {
                _state.resetValidationState(
                    inputField = { it.balanceInputFieldState },
                    validator = amountValidator::validate
                ) { copy(balanceInputFieldState = it) }
            }.launchIn(viewModelScope)

        _state.observeValidStates(
            _state.select { it.nameInputFieldState.hasError },
            _state.select { it.balanceInputFieldState.hasError },
        ) { copy(isSaveButtonEnabled = it) }.launchIn(viewModelScope)
    }
}