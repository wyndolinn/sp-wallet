package com.wynndie.spwallet.sharedFeature.transfer.presentation.screens.transferByCard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wynndie.spwallet.sharedCore.domain.constants.CoreConstants
import com.wynndie.spwallet.sharedCore.domain.constants.emptyRecipientCard
import com.wynndie.spwallet.sharedCore.domain.error.onError
import com.wynndie.spwallet.sharedCore.domain.error.onSuccess
import com.wynndie.spwallet.sharedCore.domain.models.cards.RecipientCard
import com.wynndie.spwallet.sharedCore.domain.repositories.CardsRepository
import com.wynndie.spwallet.sharedCore.domain.repositories.RecipientRepository
import com.wynndie.spwallet.sharedCore.domain.repositories.UserRepository
import com.wynndie.spwallet.sharedCore.domain.validators.BalanceValidator
import com.wynndie.spwallet.sharedFeature.transfer.domain.validators.TransferCommentValidator
import com.wynndie.spwallet.sharedCore.presentation.controllers.navigation.NavController
import com.wynndie.spwallet.sharedCore.presentation.controllers.overlay.OverlayController
import com.wynndie.spwallet.sharedCore.presentation.controllers.overlay.OverlayType
import com.wynndie.spwallet.sharedCore.presentation.extensions.asUiText
import com.wynndie.spwallet.sharedCore.presentation.formatters.UiText
import com.wynndie.spwallet.sharedCore.presentation.formatters.input.InputFilterOptions
import com.wynndie.spwallet.sharedCore.presentation.formatters.input.cutOffAt
import com.wynndie.spwallet.sharedCore.presentation.formatters.input.dropFirst
import com.wynndie.spwallet.sharedCore.presentation.formatters.input.filterBy
import com.wynndie.spwallet.sharedCore.presentation.models.cards.AuthedCardUi
import com.wynndie.spwallet.sharedCore.presentation.models.cards.RecipientCardUi
import com.wynndie.spwallet.sharedCore.presentation.states.LoadingState
import com.wynndie.spwallet.sharedFeature.transfer.domain.useCases.TransferByCardUseCase
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.transaction_succeed
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransferByCardViewModel(
    userRepository: UserRepository,
    cardsRepository: CardsRepository,
    private val recipientRepository: RecipientRepository,
    private val args: TransferByCardViewModelArgs,
    private val transferByCardUseCase: TransferByCardUseCase,
    private val transferAmountValidator: BalanceValidator,
    private val commentValidator: TransferCommentValidator,
) : ViewModel() {

    private val _state = MutableStateFlow(TransferByCardState())
    val state = _state.asStateFlow()

    private var recipients = emptyList<RecipientCard>()

    init {
        userRepository.getAuthedUsers().onEach { users ->
            val user = users.firstOrNull() ?: return@onEach
            val prefix = "${user.name}: "
            _state.update { state ->
                state.copy(
                    user = user,
                    commentInputFieldState = state.commentInputFieldState.copy(
                        maxLength = state.commentInputFieldState.maxLength - prefix.length
                    )
                )
            }
        }.launchIn(viewModelScope)

        cardsRepository.getAuthedCards().onEach { cards ->
            val card = cards.find { it.id == args.cardId }
                ?: cards.firstOrNull()
                ?: return@onEach

            _state.update { state ->
                state.copy(
                    cards = cards.map { AuthedCardUi.of(it) },
                    carouselPage = cards.indexOf(card)
                )
            }
        }.launchIn(viewModelScope)

        recipientRepository.getRecipients().onEach { recipientCards ->
            recipients = recipientCards
        }.launchIn(viewModelScope)
    }

    fun onAction(action: TransferByCardAction) {
        when (action) {

            is TransferByCardAction.OnSwipeCarousel -> {
                _state.update { it.copy(carouselPage = action.index) }
            }


            TransferByCardAction.OnToggleCalculatorSheet -> {
                _state.update { state ->
                    state.copy(isCalculatorSheetVisible = !state.isCalculatorSheetVisible)
                }
            }


            TransferByCardAction.OnClickBack -> {
                viewModelScope.launch {
                    NavController.navigate(TransferByCardNavEvent.OnClickBack)
                }
            }

            is TransferByCardAction.OnClickRecipient -> {
                viewModelScope.launch {
                    NavController.navigate(TransferByCardNavEvent.OnClickRecipient)
                }
            }

            is TransferByCardAction.OnClickTransfer -> {
                viewModelScope.launch {
                    _state.update { it.copy(loadingState = LoadingState.Loading) }

                    val selectedCard = state.value.cards[state.value.carouselPage]

                    val validationResults = listOf(
                        isTransferAmountValid(action.transferAmount),
                        isCommentValid(action.comment)
                    )

                    if (validationResults.all { it }) {
                        transferByCardUseCase(
                            card = selectedCard.toDomain(),
                            receiverCardNumber = action.cardNumber,
                            amount = action.transferAmount,
                            comment = action.comment
                        ).onError {
                            OverlayController.send(OverlayType.Snackbar(it.asUiText()))
                        }.onSuccess {
                            OverlayController.send(
                                OverlayType.Snackbar(UiText.ResourceString(Res.string.transaction_succeed))
                            )
                            NavController.navigate(TransferByCardNavEvent.OnClickBack)
                        }
                    }

                    _state.update { it.copy(loadingState = LoadingState.Finished) }
                }
            }


            is TransferByCardAction.OnChangeTransferAmountValue -> {
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

            is TransferByCardAction.OnChangeCommentValue -> {
                val value = action.value
                    .filterBy(InputFilterOptions.LettersOrDigits.predicate)
                    .cutOffAt(CoreConstants.MAX_COMMENT_LENGTH) ?: return

                _state.update { state ->
                    state.copy(
                        commentInputFieldState = state.commentInputFieldState.copy(
                            value = value
                        )
                    )
                }
            }
        }
    }

    fun updateRecipient(id: String?, cardNumber: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(loadingState = LoadingState.Loading)
            }

            if (id != _state.value.recipient.id) {
                val recipient = recipients.find { it.id == _state.value.recipient.id }
                    ?: emptyRecipientCard.copy(number = _state.value.recipient.number)

                _state.update { state ->
                    state.copy(
                        recipient = RecipientCardUi.of(recipient)
                    )
                }
            }

            _state.update { state ->
                state.copy(
                    recipient = state.recipient.copy(
                        id = id ?: "",
                        number = cardNumber
                    )
                )
            }

            _state.update {
                it.copy(loadingState = LoadingState.Finished)
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

    private fun isCommentValid(value: String): Boolean {
        val (isValid, error) = commentValidator.validate(value)
        _state.update { state ->
            state.copy(
                commentInputFieldState = state.commentInputFieldState.copy(
                    supportingText = error?.asUiText(),
                    hasError = !isValid
                )
            )
        }
        return isValid
    }
}