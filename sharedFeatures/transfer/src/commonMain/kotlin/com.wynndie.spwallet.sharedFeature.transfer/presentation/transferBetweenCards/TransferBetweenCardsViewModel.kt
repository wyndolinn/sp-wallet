package com.wynndie.spwallet.sharedFeature.transfer.presentation.transferBetweenCards

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wynndie.spwallet.sharedCore.domain.constants.CoreConstants
import com.wynndie.spwallet.sharedCore.domain.outcome.getOrElse
import com.wynndie.spwallet.sharedCore.domain.repositories.CardsRepository
import com.wynndie.spwallet.sharedCore.domain.repositories.PreferencesRepository
import com.wynndie.spwallet.sharedCore.domain.validators.BalanceValidator
import com.wynndie.spwallet.sharedCore.domain.validators.models.BalanceValidationValues
import com.wynndie.spwallet.sharedCore.presentation.controllers.navigation.NavEventController
import com.wynndie.spwallet.sharedCore.presentation.controllers.overlay.Snackbar
import com.wynndie.spwallet.sharedCore.presentation.controllers.overlay.SnackbarController
import com.wynndie.spwallet.sharedCore.presentation.extensions.asUiText
import com.wynndie.spwallet.sharedCore.presentation.extensions.observeInputField
import com.wynndie.spwallet.sharedCore.presentation.extensions.observeValidationStates
import com.wynndie.spwallet.sharedCore.presentation.extensions.validateInputField
import com.wynndie.spwallet.sharedCore.presentation.formatters.LoadingState
import com.wynndie.spwallet.sharedCore.presentation.formatters.UiText.ResourceString
import com.wynndie.spwallet.sharedCore.presentation.formatters.input.InputFilters
import com.wynndie.spwallet.sharedCore.presentation.formatters.input.cutOffAt
import com.wynndie.spwallet.sharedCore.presentation.formatters.input.dropFirst
import com.wynndie.spwallet.sharedCore.presentation.formatters.input.filterBy
import com.wynndie.spwallet.sharedFeature.transfer.domain.models.TransferCard
import com.wynndie.spwallet.sharedFeature.transfer.domain.useCases.TransferByCardUseCase
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.transaction_succeed
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransferBetweenCardsViewModel(
    cardsRepository: CardsRepository,
    preferencesRepository: PreferencesRepository,
    private val args: TransferBetweenCardsParams,
    private val transferByCardUseCase: TransferByCardUseCase,
    private val transferAmountValidator: BalanceValidator,
    private val navEventController: NavEventController,
    private val snackbarController: SnackbarController
) : ViewModel() {

    private val _state = MutableStateFlow(TransferBetweenCardsState())
    val state = _state.map { state ->
        state.sourceCards.getOrNull(state.selectedSourceCard)?.let { sourceCard ->
            state.copy(
                destinationCards = state.destinationCards.filter {
                    it.id != sourceCard.id
                }
            )
        } ?: state
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = _state.value
    )

    init {
        combine(
            cardsRepository.getAuthedCards(),
            cardsRepository.getUnauthedCards(),
            preferencesRepository.getSelectedSpServer()
        ) { authedCards, unauthedCards, selectedServer ->

            val destinationsCards = buildList {
                addAll(authedCards.filter { it.server == selectedServer }.map(TransferCard::of))
                addAll(unauthedCards.filter { it.server == selectedServer }.map(TransferCard::of))
            }
            val destinationCard = destinationsCards.find { it.id == args.destinationCardId }

            _state.update { state ->
                state.copy(
                    sourceCards = authedCards.filter { it.server == selectedServer },
                    destinationCards = destinationCard?.let { listOf(it) } ?: destinationsCards
                )
            }
        }.launchIn(viewModelScope)

        observeValidationStates(
            _state.observeInputField(
                inputField = { it.amountInputFieldState },
                validation = {
                    val validationValues = BalanceValidationValues(
                        value = _state.value.amountInputFieldState.value.text
                    )
                    transferAmountValidator.validate(validationValues)
                },
                updateState = { value -> _state.update { it.copy(amountInputFieldState = value) } }
            )
        ).onEach { isAllValid ->
            _state.update { it.copy(isTransferButtonEnabled = isAllValid) }
        }.launchIn(viewModelScope)
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
                comment = "Перевод между счетами"
            ).getOrElse { error ->
                snackbarController.send(Snackbar(error.asUiText()))
                _state.update { it.copy(loadingState = LoadingState.Finished) }
                return@launch
            }

            snackbarController.send(Snackbar(ResourceString(Res.string.transaction_succeed)))
            navEventController.navigate(TransferBetweenCardsNavEvent.NavigateToResult)

            _state.update { it.copy(loadingState = LoadingState.Finished) }
        }
    }

    private fun changeAmountValue(value: TextFieldValue) {
        val value = value
            .filterBy(InputFilters.DigitsOnly.predicate)
            .dropFirst('0')
            .cutOffAt(CoreConstants.MAX_BALANCE_LENGTH) ?: return

        _state.update { state ->
            state.copy(
                amountInputFieldState = state.amountInputFieldState.copy(
                    value = value
                )
            )
        }
    }

    private fun clearAmountFocus() {
        _state.validateInputField(
            inputField = { it.amountInputFieldState },
            validation = {
                val validationValues = BalanceValidationValues(
                    value = _state.value.amountInputFieldState.value.text,
                    maxValue = _state.value.sourceCards[_state.value.selectedSourceCard].balance
                )
                transferAmountValidator.validate(validationValues)
            },
            updateState = { value -> _state.update { it.copy(amountInputFieldState = value) } }
        )
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