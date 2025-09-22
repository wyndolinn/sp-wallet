package com.wynndie.spwallet.sharedFeature.transfer.presentation.screen.transferBetweenCards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wynndie.spwallet.sharedCore.domain.constants.Constants
import com.wynndie.spwallet.sharedCore.domain.error.onError
import com.wynndie.spwallet.sharedCore.domain.error.onSuccess
import com.wynndie.spwallet.sharedCore.domain.repository.CardsRepository
import com.wynndie.spwallet.sharedCore.domain.validator.BalanceValidator
import com.wynndie.spwallet.sharedCore.presentation.controller.overlay.OverlayController
import com.wynndie.spwallet.sharedCore.presentation.controller.overlay.OverlayType
import com.wynndie.spwallet.sharedCore.presentation.mapper.asUiText
import com.wynndie.spwallet.sharedCore.presentation.model.LoadingState
import com.wynndie.spwallet.sharedCore.presentation.model.UiText
import com.wynndie.spwallet.sharedCore.presentation.model.card.UiAuthedCard
import com.wynndie.spwallet.sharedCore.presentation.model.card.UiUnauthedCard
import com.wynndie.spwallet.sharedCore.presentation.model.input.InputFilterOptions
import com.wynndie.spwallet.sharedCore.presentation.model.input.cutOffAt
import com.wynndie.spwallet.sharedCore.presentation.model.input.dropFirst
import com.wynndie.spwallet.sharedCore.presentation.model.input.filterBy
import com.wynndie.spwallet.sharedFeature.transfer.domain.usecase.TransferByCardUseCase
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.transaction_succeed
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransferBetweenCardsViewModel(
    userRepository: CardsRepository,
    cardsRepository: CardsRepository,
    private val args: TransferBetweenCardsArgs,
    private val transferByCardUseCase: TransferByCardUseCase,
    private val transferAmountValidator: BalanceValidator
) : ViewModel() {

    private val _state = MutableStateFlow(TransferBetweenCardsState())
    val state = _state.asStateFlow()

    init {
        combine(
            cardsRepository.getAuthedCards(),
            cardsRepository.getUnauthedCards()
        ) { authedCards, unauthedCards ->

            val destinationsCards = buildList {
                addAll(authedCards.map(UiAuthedCard::of))
                addAll(unauthedCards.map(UiUnauthedCard::of))
            }
            val destinationCard = args.destinationCardId?.let { id ->
                destinationsCards.find { it.id == id }
            }

            _state.update { state ->
                state.copy(
                    sourceCards = authedCards.map(UiAuthedCard::of),
                    destinationCards = destinationCard?.let { listOf(it) } ?: destinationsCards
                )
            }
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
                args.onClickBack()
            }

            is TransferBetweenCardsAction.OnClickTransferAction -> {
                viewModelScope.launch {
                    _state.update { it.copy(loadingState = LoadingState.Loading) }

                    val sourceCard = state.value.sourceCards[state.value.sourceCardsCarouselPage]
                    val transferAmount = state.value.amountInputFieldState.value.text

                    if (isTransferAmountValid(transferAmount)) {
                        transferByCardUseCase(
                            card = sourceCard.toDomain(),
                            receiverCardNumber = action.cardNumber,
                            amount = transferAmount,
                            comment = action.comment
                        ).onError {
                            OverlayController.send(OverlayType.Snackbar(it.asUiText()))
                        }.onSuccess {
                            OverlayController.send(
                                OverlayType.Snackbar(UiText.StringResourceId(Res.string.transaction_succeed))
                            )
                            args.onClickBack()
                        }
                    }

                    _state.update { it.copy(loadingState = LoadingState.Finished) }
                }
            }


            is TransferBetweenCardsAction.OnChangeTransferAmountValueAction -> {
                val value = action.value
                    .filterBy(InputFilterOptions.Digits.DigitsOnly.predicate)
                    .dropFirst("0")
                    .cutOffAt(Constants.MAX_BALANCE_LENGTH) ?: return

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
        }
    }


    private fun isTransferAmountValid(value: String): Boolean {
        val (isValid, error) = transferAmountValidator.validate(value)
        _state.update { state ->
            state.copy(
                amountInputFieldState = state.amountInputFieldState.copy(
                    supportingText = error?.asUiText(),
                    hasError = !isValid
                )
            )
        }
        return isValid
    }
}