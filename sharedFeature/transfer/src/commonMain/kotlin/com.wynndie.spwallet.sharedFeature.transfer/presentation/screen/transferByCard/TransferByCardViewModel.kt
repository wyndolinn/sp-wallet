package com.wynndie.spwallet.sharedFeature.transfer.presentation.screen.transferByCard

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wynndie.spwallet.sharedCore.domain.error.onError
import com.wynndie.spwallet.sharedCore.domain.error.onSuccess
import com.wynndie.spwallet.sharedCore.presentation.controller.dialog.Dialog
import com.wynndie.spwallet.sharedCore.presentation.controller.dialog.DialogController
import com.wynndie.spwallet.sharedCore.presentation.mapper.asUiText
import com.wynndie.spwallet.sharedCore.presentation.model.LoadingState
import com.wynndie.spwallet.sharedCore.presentation.model.UiText
import com.wynndie.spwallet.sharedCore.presentation.model.input.InputFilterOptions
import com.wynndie.spwallet.sharedCore.domain.constants.Constants
import com.wynndie.spwallet.sharedCore.domain.repository.CardsRepository
import com.wynndie.spwallet.sharedCore.domain.repository.RecipientRepository
import com.wynndie.spwallet.sharedCore.domain.repository.UserRepository
import com.wynndie.spwallet.sharedCore.domain.validator.BalanceValidator
import com.wynndie.spwallet.sharedCore.domain.validator.TransferCommentValidator
import com.wynndie.spwallet.sharedCore.presentation.model.card.UiAuthedCard
import com.wynndie.spwallet.sharedCore.presentation.model.UiRecipient
import com.wynndie.spwallet.sharedCore.presentation.model.emptyUiRecipient
import com.wynndie.spwallet.sharedCore.presentation.model.input.cutOffAt
import com.wynndie.spwallet.sharedCore.presentation.model.input.dropFirst
import com.wynndie.spwallet.sharedCore.presentation.model.input.filterBy
import com.wynndie.spwallet.sharedFeature.transfer.domain.usecase.TransferByCardUseCase
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.transaction_succeed
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransferByCardViewModel(
    userRepository: UserRepository,
    cardsRepository: CardsRepository,
    recipientRepository: RecipientRepository,
    private val args: TransferByCardViewModelArgs,
    private val transferByCardUseCase: TransferByCardUseCase,
    private val transferAmountValidator: BalanceValidator,
    private val commentValidator: TransferCommentValidator,
) : ViewModel() {

    private val _state = MutableStateFlow(TransferByCardState())
    val state = _state.asStateFlow()

    init {
        userRepository.getAuthedUsers().onEach { users ->
            val user = users.firstOrNull() ?: return@onEach
            val prefix = "${user.name}: "
            _state.update { state ->
                state.copy(
                    user = user,
                    commentInputFieldState = state.commentInputFieldState.copy(
                        maxLength = state.commentInputFieldState.maxLength - prefix.length,
                        prefix = prefix
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
                    cards = cards.map { UiAuthedCard.of(it) },
                    carouselPage = cards.indexOf(card)
                )
            }
        }.launchIn(viewModelScope)

        recipientRepository.getRecipients().onEach { _ ->
            val recipient = emptyUiRecipient.toDomain()
            _state.update { state ->
                state.copy(
                    recipient = UiRecipient.of(recipient),
                    amountInputFieldState = state.amountInputFieldState.copy(
                        value = TextFieldValue("0")
                    )
                )
            }
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
                args.onClickBack()
            }

            is TransferByCardAction.OnClickRecipient -> {
                args.onClickRecipient()
            }

            is TransferByCardAction.OnClickTransfer -> {
                viewModelScope.launch {
                    _state.update { it.copy(loadingState = LoadingState.Loading) }

                    val selectedCard = state.value.cards[state.value.carouselPage]

                    val validationResults = listOf(
                        isTransferAmountValid(action.transferAmount),
                        isCommentValid(action.comment)
                    )

                    if (!validationResults.any { isValid -> !isValid }) {
                        transferByCardUseCase(
                            card = selectedCard.toDomain(),
                            receiverCardNumber = action.cardNumber,
                            amount = action.transferAmount,
                            comment = action.comment
                        ).onError {
                            DialogController.send(Dialog.Snackbar(it.asUiText()))
                        }.onSuccess {
                            DialogController.send(
                                Dialog.Snackbar(UiText.StringResourceId(Res.string.transaction_succeed))
                            )
                            args.onClickBack()
                        }
                    }

                    _state.update { it.copy(loadingState = LoadingState.Finished) }
                }
            }


            is TransferByCardAction.OnChangeTransferAmountValue -> {
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

            is TransferByCardAction.OnChangeCommentValue -> {
                val value = action.value
                    .filterBy(InputFilterOptions.Text.LettersOrDigits.predicate)
                    .cutOffAt(Constants.MAX_COMMENT_LENGTH) ?: return

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