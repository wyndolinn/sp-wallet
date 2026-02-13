package com.wynndie.spwallet.sharedFeature.transfer.presentation.screens.transferBetweenCards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wynndie.spwallet.sharedCore.domain.constants.CoreConstants
import com.wynndie.spwallet.sharedCore.domain.error.getOrElse
import com.wynndie.spwallet.sharedCore.domain.repositories.CardsRepository
import com.wynndie.spwallet.sharedCore.domain.repositories.PreferencesRepository
import com.wynndie.spwallet.sharedCore.domain.validators.BalanceValidator
import com.wynndie.spwallet.sharedCore.domain.validators.models.BalanceValidationValues
import com.wynndie.spwallet.sharedCore.presentation.controllers.navigation.NavController
import com.wynndie.spwallet.sharedCore.presentation.controllers.overlay.OverlayController
import com.wynndie.spwallet.sharedCore.presentation.controllers.overlay.OverlayType.Snackbar
import com.wynndie.spwallet.sharedCore.presentation.extensions.asUiText
import com.wynndie.spwallet.sharedCore.presentation.extensions.observeInputField
import com.wynndie.spwallet.sharedCore.presentation.extensions.observeValidationStates
import com.wynndie.spwallet.sharedCore.presentation.extensions.validateInputField
import com.wynndie.spwallet.sharedCore.presentation.formatters.UiText.ResourceString
import com.wynndie.spwallet.sharedCore.presentation.formatters.input.InputFilterOptions
import com.wynndie.spwallet.sharedCore.presentation.formatters.input.cutOffAt
import com.wynndie.spwallet.sharedCore.presentation.formatters.input.dropFirst
import com.wynndie.spwallet.sharedCore.presentation.formatters.input.filterBy
import com.wynndie.spwallet.sharedCore.presentation.models.cards.AuthedCardUi
import com.wynndie.spwallet.sharedCore.presentation.models.cards.UnauthedCardUi
import com.wynndie.spwallet.sharedCore.presentation.states.LoadingState
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
    private val args: TransferBetweenCardsArgs,
    private val transferByCardUseCase: TransferByCardUseCase,
    private val transferAmountValidator: BalanceValidator
) : ViewModel() {

    private val _state = MutableStateFlow(TransferBetweenCardsState())
    val state = _state.map { state ->
        state.sourceCards.getOrNull(state.sourceCardsCarouselPage)?.let { sourceCard ->
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
                addAll(authedCards.filter { it.server == selectedServer }.map(AuthedCardUi::of))
                addAll(unauthedCards.filter { it.server == selectedServer }.map(UnauthedCardUi::of))
            }
            val destinationCard = args.destinationCardId?.let { id ->
                destinationsCards.find { it.id == id }
            }

            _state.update { state ->
                state.copy(
                    sourceCards = authedCards.filter { it.server == selectedServer }
                        .map(AuthedCardUi::of),
                    destinationCards = destinationCard?.let { listOf(it) } ?: destinationsCards
                )
            }
        }.launchIn(viewModelScope)

        observeValidationStates(
            _state.observeInputField(
                inputField = { it.amountInputFieldState },
                validation = {
                    val validationValues = BalanceValidationValues(
                        value = _state.value.amountInputFieldState.value.text,
                        maxValue = _state.value.sourceCards[_state.value.sourceCardsCarouselPage].balance.value
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
            is TransferBetweenCardsAction.OnSwipeSourceCardsCarousel -> {
                _state.update { it.copy(sourceCardsCarouselPage = action.index) }
            }

            is TransferBetweenCardsAction.OnSwipeDestinationCardsCarousel -> {
                _state.update { it.copy(destinationCardsCarouselPage = action.index) }
            }


            TransferBetweenCardsAction.OnClickBack -> {
                viewModelScope.launch {
                    NavController.navigate(TransferBetweenCardsNavEvent.OnClickBack)
                }
            }

            is TransferBetweenCardsAction.OnClickTransferAction -> {
                viewModelScope.launch {
                    _state.update { it.copy(loadingState = LoadingState.Loading) }

                    val sourceCard = state.value.sourceCards[state.value.sourceCardsCarouselPage]
                    val targetCard =
                        state.value.destinationCards[state.value.destinationCardsCarouselPage]
                    val transferAmount = state.value.amountInputFieldState.value.text

                    transferByCardUseCase(
                        card = sourceCard.toDomain(),
                        receiverCardNumber = targetCard.number ?: "",
                        amount = transferAmount,
                        comment = "Перевод между счетами"
                    ).getOrElse { error ->
                        OverlayController.send(Snackbar(error.asUiText()))
                        _state.update { it.copy(loadingState = LoadingState.Finished) }
                        return@launch
                    }

                    OverlayController.send(Snackbar(ResourceString(Res.string.transaction_succeed)))
                    NavController.navigate(TransferBetweenCardsNavEvent.OnTransferSuccess)

                    _state.update { it.copy(loadingState = LoadingState.Finished) }
                }
            }


            is TransferBetweenCardsAction.OnChangeTransferAmountValueAction -> {
                val value = action.value
                    .filterBy(InputFilterOptions.DigitsOnly.predicate)
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


            TransferBetweenCardsAction.OnToggleCalculatorSheet -> {
                _state.update { state ->
                    state.copy(
                        isCalculatorSheetVisible = !state.isCalculatorSheetVisible
                    )
                }
            }

            TransferBetweenCardsAction.OnToggleTransferAmountFocus -> {
                _state.validateInputField(
                    inputField = { it.amountInputFieldState },
                    validation = {
                        val validationValues = BalanceValidationValues(
                            value = _state.value.amountInputFieldState.value.text,
                            maxValue = _state.value.sourceCards[_state.value.sourceCardsCarouselPage].balance.value
                        )
                        transferAmountValidator.validate(validationValues)
                    },
                    updateState = { value -> _state.update { it.copy(amountInputFieldState = value) } }
                )
            }
        }
    }
}