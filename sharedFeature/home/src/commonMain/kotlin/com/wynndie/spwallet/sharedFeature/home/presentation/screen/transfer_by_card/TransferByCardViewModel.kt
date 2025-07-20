package com.wynndie.spwallet.sharedFeature.home.presentation.screen.transfer_by_card

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
import com.wynndie.spwallet.sharedCore.presentation.model.input.InputFormatter
import com.wynndie.spwallet.sharedCore.domain.constants.Constants
import com.wynndie.spwallet.sharedFeature.home.domain.repository.WalletRepository
import com.wynndie.spwallet.sharedFeature.home.domain.usecase.TransferByCardUseCase
import com.wynndie.spwallet.sharedCore.domain.validator.BalanceValidator
import com.wynndie.spwallet.sharedCore.domain.validator.CardNumberValidator
import com.wynndie.spwallet.sharedCore.domain.validator.TransferCommentValidator
import com.wynndie.spwallet.sharedCore.presentation.model.card.UiAuthedCard
import com.wynndie.spwallet.sharedCore.presentation.model.UiRecipient
import com.wynndie.spwallet.sharedCore.presentation.model.emptyRecipientCard
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.transaction_succeed
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransferByCardViewModel(
    private val args: TransferByCardViewModelArgs,
    walletRepository: WalletRepository,
    private val transferByCardUseCase: TransferByCardUseCase,
    private val cardNumberValidator: CardNumberValidator,
    private val transferAmountValidator: BalanceValidator,
    private val commentValidator: TransferCommentValidator,
) : ViewModel() {

    private val _state = MutableStateFlow(TransferByCardState())
    val state = _state.asStateFlow()

    init {
        walletRepository.getAuthedUsers().onEach { users ->
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

        walletRepository.getAuthedCards().onEach { cards ->
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

        walletRepository.getRecipients().onEach { _ ->
            val recipient = emptyRecipientCard.toDomain()
            _state.update { state ->
                state.copy(
                    recipient = UiRecipient.of(recipient),
                    recipientInputFieldState = state.recipientInputFieldState.copy(
                        value = TextFieldValue(recipient.number)
                    ),
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

            TransferByCardAction.OnToggleRecipientSheet -> {
                _state.update { state ->
                    state.copy(isChangeRecipientSheetVisible = !state.isChangeRecipientSheetVisible)
                }
            }


            TransferByCardAction.OnClickBack -> {
                args.onClickBack()
            }

            is TransferByCardAction.OnClickRecipient -> {
                _state.update { state ->
                    state.copy(
                        isChangeRecipientSheetVisible = false,
                    )
                }
            }

            is TransferByCardAction.OnClickTransfer -> {
                viewModelScope.launch {
                    _state.update { it.copy(loadingState = LoadingState.Loading) }

                    val selectedCard = state.value.cards[state.value.carouselPage]

                    val validationResults = listOf(
                        isCardNumberValid(action.cardNumber),
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


            is TransferByCardAction.OnChangeRecipientValue -> {
                val inputFormatter = InputFormatter(action.value)
                    .filterBy(InputFilterOptions.Digits.DigitsOnly.predicate)
                    .cutOffAt(Constants.CARD_NUMBER_LENGTH) ?: return

                _state.update { state ->
                    state.copy(
                        recipientInputFieldState = state.recipientInputFieldState.copy(
                            value = inputFormatter.value
                        ),
                        recipient = state.recipient.copy(
                            number = inputFormatter.value.text
                        )
                    )
                }
            }

            is TransferByCardAction.OnChangeTransferAmountValue -> {
                val inputFormatter = InputFormatter(action.value)
                    .filterBy(InputFilterOptions.Digits.DigitsOnly.predicate)
                    .dropFirst("0")
                    .cutOffAt(Constants.MAX_BALANCE_LENGTH) ?: return

                _state.update { state ->
                    state.copy(
                        amountInputFieldState = state.amountInputFieldState.copy(
                            value = inputFormatter.value
                        )
                    )
                }
            }

            is TransferByCardAction.OnChangeCommentValue -> {
                val inputFormatter = InputFormatter(action.value)
                    .filterBy(InputFilterOptions.Text.LettersOrDigits.predicate)
                    .cutOffAt(Constants.MAX_COMMENT_LENGTH) ?: return

                _state.update { state ->
                    state.copy(
                        commentInputFieldState = state.commentInputFieldState.copy(
                            value = inputFormatter.value
                        )
                    )
                }
            }
        }
    }


    private fun isCardNumberValid(value: String): Boolean {
        val (isValid, error) = cardNumberValidator.validate(value)
        _state.update { state ->
            state.copy(
                recipientInputFieldState = state.recipientInputFieldState.copy(
                    supportingText = error?.asUiText(),
                    hasError = !isValid
                )
            )
        }
        return isValid
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