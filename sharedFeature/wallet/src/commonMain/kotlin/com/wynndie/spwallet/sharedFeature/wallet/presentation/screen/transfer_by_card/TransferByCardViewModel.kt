package com.wynndie.spwallet.sharedFeature.wallet.presentation.screen.transfer_by_card

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wynndie.spwallet.sharedCore.domain.error.getOrNull
import com.wynndie.spwallet.sharedCore.domain.error.onError
import com.wynndie.spwallet.sharedCore.domain.error.onSuccess
import com.wynndie.spwallet.sharedCore.presentation.controller.dialog.Dialog
import com.wynndie.spwallet.sharedCore.presentation.controller.dialog.DialogController
import com.wynndie.spwallet.sharedCore.presentation.mapper.asUiText
import com.wynndie.spwallet.sharedCore.presentation.model.input.FilterOptions
import com.wynndie.spwallet.sharedCore.presentation.model.input.InputFormatter
import com.wynndie.spwallet.sharedCore.presentation.model.LoadingState
import com.wynndie.spwallet.sharedCore.presentation.model.UiText
import com.wynndie.spwallet.sharedFeature.wallet.domain.constants.Constants
import com.wynndie.spwallet.sharedFeature.wallet.domain.repository.WalletRepository
import com.wynndie.spwallet.sharedFeature.wallet.domain.usecase.TransferByCardUseCase
import com.wynndie.spwallet.sharedFeature.wallet.domain.validator.BalanceValidator
import com.wynndie.spwallet.sharedFeature.wallet.domain.validator.CardNumberValidator
import com.wynndie.spwallet.sharedFeature.wallet.domain.validator.TransferCommentValidator
import com.wynndie.spwallet.sharedFeature.wallet.presentation.model.UiAuthedCard
import com.wynndie.spwallet.sharedFeature.wallet.presentation.model.UiRecipientCard
import com.wynndie.spwallet.sharedFeature.wallet.presentation.model.emptyRecipientCard
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.transaction_succeed
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransferByCardViewModel(
    args: TransferByCardViewModelArgs,
    private val walletRepository: WalletRepository,
    private val transferByCardUseCase: TransferByCardUseCase,
    private val cardNumberValidator: CardNumberValidator,
    private val transferAmountValidator: BalanceValidator,
    private val commentValidator: TransferCommentValidator,
) : ViewModel() {

    private val _state = MutableStateFlow(TransferByCardState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            walletRepository.getAuthedUsers().onEach { users ->
                val user = users.firstOrNull() ?: return@onEach
                _state.update { it.copy(user = user) }
            }.launchIn(this)
        }

        viewModelScope.launch {
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
            }.launchIn(this)
        }

        viewModelScope.launch {
            walletRepository.getRecipients().onEach { _ ->
                val recipient = emptyRecipientCard.toDomain()
                _state.update { state ->
                    state.copy(
                        recipient = UiRecipientCard.of(recipient),
                        recipientInputField = state.recipientInputField.copy(
                            value = TextFieldValue(recipient.number)
                        ),
                        amountInputField = state.amountInputField.copy(
                            value = TextFieldValue("0")
                        )
                    )
                }
            }.launchIn(this)
        }
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
                            action.navigateBack()
                        }
                    }

                    _state.update { it.copy(loadingState = LoadingState.Finished) }
                }
            }


            is TransferByCardAction.OnChangeRecipientValue -> {
                val inputFormatter = InputFormatter(action.value)
                    .filterBy(FilterOptions.Digits.DigitsOnly.predicate)
                    .cutOffAt(Constants.CARD_NUMBER_LENGTH) ?: return

                _state.update { state ->
                    state.copy(
                        recipientInputField = state.recipientInputField.copy(
                            value = inputFormatter.value
                        ),
                        recipient = state.recipient.copy(
                            title = UiText.DynamicString(inputFormatter.value.text),
                            number = inputFormatter.value.text
                        )
                    )
                }
            }

            is TransferByCardAction.OnChangeTransferAmountValue -> {
                val inputFormatter = InputFormatter(action.value)
                    .filterBy(FilterOptions.Digits.DigitsOnly.predicate)
                    .dropFirst("0")
                    .cutOffAt(Constants.MAX_BALANCE_LENGTH) ?: return

                _state.update { state ->
                    state.copy(
                        amountInputField = state.amountInputField.copy(
                            value = inputFormatter.value
                        )
                    )
                }
            }

            is TransferByCardAction.OnChangeCommentValue -> {
                val inputFormatter = InputFormatter(action.value)
                    .filterBy(FilterOptions.Text.LettersOrDigits.predicate)
                    .cutOffAt(Constants.MAX_COMMENT_LENGTH) ?: return

                _state.update { state ->
                    state.copy(
                        commentInputField = state.commentInputField.copy(
                            value = inputFormatter.value
                        )
                    )
                }
            }
        }
    }


    private fun isCardNumberValid(value: String): Boolean {
        return cardNumberValidator.validate(value)
            .onError { error ->
                _state.update {
                    it.copy(
                        recipientInputField = it.recipientInputField.copy(
                            supportingText = error.asUiText(),
                            isError = true
                        )
                    )
                }
            }
            .onSuccess {
                _state.update {
                    it.copy(
                        recipientInputField = it.recipientInputField.copy(
                            supportingText = UiText.DynamicString(""),
                            isError = false
                        )
                    )
                }
            }
            .getOrNull() ?: false
    }

    private fun isTransferAmountValid(value: String): Boolean {
        return transferAmountValidator.validate(value)
            .onError { error ->
                _state.update {
                    it.copy(
                        amountInputField = it.amountInputField.copy(
                            supportingText = error.asUiText(),
                            isError = true
                        )
                    )
                }
            }
            .onSuccess {
                _state.update {
                    it.copy(
                        amountInputField = it.amountInputField.copy(
                            supportingText = UiText.DynamicString(""),
                            isError = false
                        )
                    )
                }
            }
            .getOrNull() ?: false
    }

    private fun isCommentValid(value: String): Boolean {
        return commentValidator.validate(value)
            .onError { error ->
                _state.update {
                    it.copy(
                        commentInputField = it.commentInputField.copy(
                            supportingText = error.asUiText(),
                            isError = true
                        )
                    )
                }
            }
            .onSuccess {
                _state.update {
                    it.copy(
                        commentInputField = it.commentInputField.copy(
                            supportingText = UiText.DynamicString(""),
                            isError = false
                        )
                    )
                }
            }
            .getOrNull() ?: false
    }
}