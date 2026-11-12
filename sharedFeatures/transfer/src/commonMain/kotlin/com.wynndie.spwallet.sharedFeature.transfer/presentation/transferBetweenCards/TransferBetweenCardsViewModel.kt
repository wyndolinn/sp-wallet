package com.wynndie.spwallet.sharedFeature.transfer.presentation.transferBetweenCards

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wynndie.spwallet.sharedCore.Res
import com.wynndie.spwallet.sharedCore.domain.helpers.CoreConstants
import com.wynndie.spwallet.sharedCore.domain.outcome.getOrElse
import com.wynndie.spwallet.sharedCore.domain.repositories.CardsRepository
import com.wynndie.spwallet.sharedCore.domain.repositories.PreferencesRepository
import com.wynndie.spwallet.sharedCore.domain.validators.AmountValidator
import com.wynndie.spwallet.sharedCore.domain.validators.core.ValidationValues
import com.wynndie.spwallet.sharedCore.presentation.controllers.navigation.NavEventController
import com.wynndie.spwallet.sharedCore.presentation.controllers.overlay.SnackbarController
import com.wynndie.spwallet.sharedCore.presentation.extensions.cutOffAt
import com.wynndie.spwallet.sharedCore.presentation.extensions.dropWhile
import com.wynndie.spwallet.sharedCore.presentation.extensions.filter
import com.wynndie.spwallet.sharedCore.presentation.extensions.observeValidStates
import com.wynndie.spwallet.sharedCore.presentation.extensions.resetValidationState
import com.wynndie.spwallet.sharedCore.presentation.extensions.select
import com.wynndie.spwallet.sharedCore.presentation.extensions.validateInputField
import com.wynndie.spwallet.sharedCore.presentation.formatters.LoadingState
import com.wynndie.spwallet.sharedCore.presentation.formatters.asUiText
import com.wynndie.spwallet.sharedCore.presentation.models.InputFilters
import com.wynndie.spwallet.sharedCore.presentation.models.UiText.ResourceString
import com.wynndie.spwallet.sharedCore.transaction_succeed
import com.wynndie.spwallet.sharedFeature.transfer.domain.models.TransferCard
import com.wynndie.spwallet.sharedFeature.transfer.domain.useCases.TransferByCardUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransferBetweenCardsViewModel(
    private val cardsRepository: CardsRepository,
    private val preferencesRepository: PreferencesRepository,
    private val args: TransferBetweenCardsParams,
    private val transferByCardUseCase: TransferByCardUseCase,
    private val transferAmountValidator: AmountValidator,
    private val navEventController: NavEventController,
    private val snackbarController: SnackbarController,
) : ViewModel() {

    private val _state = MutableStateFlow(TransferBetweenCardsState())
    val state = _state.map { state ->
        state.sourceCards.getOrNull(state.selectedSourceCard)?.let { sourceCard ->
            state.copy(destinationCards = state.destinationCards.filter { it.id != sourceCard.id })
        } ?: state
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = _state.value,
    )


    init {
        observeCards()
        observeInputValidation()
    }


    private fun observeCards() {
        combine(
            cardsRepository.getAuthedCards(),
            cardsRepository.getUnauthedCards(),
            preferencesRepository.getSelectedServer(),
        ) { authedCards, unauthedCards, selectedServer ->

            val authedCards = authedCards.filter { it.server == selectedServer }
            val destinationsCards = buildList {
                addAll(authedCards.map(TransferCard::of))
                addAll(unauthedCards.filter { it.server == selectedServer }.map(TransferCard::of))
            }

            val sourceCard = authedCards.find { it.id == args.sourceCardId }
            _state.update { state ->
                state.copy(
                    sourceCards = sourceCard?.let { listOf(it) } ?: authedCards,
                    destinationCards = destinationsCards,
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun observeInputValidation() {
        combine(
            _state.select { it.amountInputFieldState.value },
            _state.select { it.sourceCards },
            _state.select { it.selectedSourceCard }
        ) { _, cards, cardIndex ->
            if (cards.isEmpty()) return@combine

            _state.resetValidationState(
                inputField = { it.amountInputFieldState },
                validator = {
                    transferAmountValidator.validate(it.copy(maxValue = cards[cardIndex].balance))
                }
            ) { copy(amountInputFieldState = it) }
        }.launchIn(viewModelScope)

        _state.observeValidStates(
            _state.select { it.amountInputFieldState.hasError },
        ) { copy(isTransferButtonEnabled = it) }.launchIn(viewModelScope)
    }

    fun onAction(action: TransferBetweenCardsAction) {
        when (action) {
            TransferBetweenCardsAction.NavigateBack -> goBack()
            TransferBetweenCardsAction.MakeTransfer -> makeTransfer()
            is TransferBetweenCardsAction.SelectSourceCard -> selectSourceCard(action.index)
            is TransferBetweenCardsAction.SelectDestinationCard -> selectDestinationCard(action.index)
            is TransferBetweenCardsAction.ChangeAmountValue -> changeAmountValue(action.value)
            TransferBetweenCardsAction.ClearAmountFocus -> clearAmountFocus()
        }
    }

    private fun makeTransfer() {
        viewModelScope.launch {
            _state.update { it.copy(loadingState = LoadingState.Loading) }

            val sourceCard = state.value.sourceCards[state.value.selectedSourceCard]
            val targetCard =
                state.value.destinationCards[state.value.selectedDestinationCard]
            val transferAmount = state.value.amountInputFieldState.value.text

            transferByCardUseCase(
                card = sourceCard,
                receiver = targetCard.number,
                amount = transferAmount,
                comment = "Перевод между счетами",
            ).getOrElse { error ->
                snackbarController.send(error.asUiText())
                _state.update { it.copy(loadingState = LoadingState.Finished) }
                return@launch
            }

            snackbarController.send(ResourceString(Res.string.transaction_succeed))
            navEventController.navigate(TransferBetweenCardsNavEvent.NavigateToResult)

            _state.update { it.copy(loadingState = LoadingState.Finished) }
        }
    }

    private fun changeAmountValue(value: TextFieldValue) {
        _state.update { state ->
            val filteredValue = value
                .filter(InputFilters.Numbers)
                .dropWhile('0') { it.length > 1 }
                .cutOffAt(CoreConstants.MAX_BALANCE_LENGTH) ?: return@update state

            state.copy(
                amountInputFieldState = state.amountInputFieldState.copy(
                    value = filteredValue,
                ),
            )
        }
    }

    private fun clearAmountFocus() {
        val value = _state.value.amountInputFieldState.value.text
        val maxValue = _state.value.sourceCards[_state.value.selectedSourceCard].balance

        _state.validateInputField(
            inputField = { it.amountInputFieldState },
            validator = {
                val validationValues = ValidationValues(value = value, maxValue = maxValue)
                transferAmountValidator.validate(validationValues)
            }
        ) { copy(amountInputFieldState = it) }
    }

    private fun goBack() {
        viewModelScope.launch {
            navEventController.navigate(TransferBetweenCardsNavEvent.NavigateBack)
        }
    }

    private fun selectSourceCard(index: Int) {
        _state.update { it.copy(selectedSourceCard = index) }
    }

    private fun selectDestinationCard(index: Int) {
        _state.update { it.copy(selectedDestinationCard = index) }
    }
}