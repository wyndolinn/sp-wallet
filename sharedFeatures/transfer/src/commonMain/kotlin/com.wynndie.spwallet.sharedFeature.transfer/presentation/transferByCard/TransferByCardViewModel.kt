package com.wynndie.spwallet.sharedFeature.transfer.presentation.transferByCard

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wynndie.spwallet.sharedCore.domain.constants.CoreConstants
import com.wynndie.spwallet.sharedCore.domain.constants.emptyRecipientCard
import com.wynndie.spwallet.sharedCore.domain.outcome.onError
import com.wynndie.spwallet.sharedCore.domain.outcome.onSuccess
import com.wynndie.spwallet.sharedCore.domain.repositories.CardsRepository
import com.wynndie.spwallet.sharedCore.domain.repositories.PreferencesRepository
import com.wynndie.spwallet.sharedCore.domain.repositories.RecipientRepository
import com.wynndie.spwallet.sharedCore.domain.repositories.UserRepository
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
import com.wynndie.spwallet.sharedFeature.transfer.domain.useCases.TransferByCardUseCase
import com.wynndie.spwallet.sharedFeature.transfer.domain.validators.TransferCommentValidator
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.transaction_succeed
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransferByCardViewModel(
    userRepository: UserRepository,
    cardsRepository: CardsRepository,
    private val args: TransferByCardParams,
    private val recipientRepository: RecipientRepository,
    private val preferencesRepository: PreferencesRepository,
    private val transferByCardUseCase: TransferByCardUseCase,
    private val transferAmountValidator: BalanceValidator,
    private val commentValidator: TransferCommentValidator,
    private val navEventController: NavEventController,
    private val snackbarController: SnackbarController
) : ViewModel() {

    private val _state = MutableStateFlow(TransferByCardState())
    val state = _state.asStateFlow()

    private var commentPrefix = ""

    init {
        userRepository.getAuthedUsers().onEach { users ->
            val user = users.firstOrNull() ?: return@onEach
            commentPrefix = "${user.name}: "
            _state.update { state ->
                state.copy(
                    user = user,
                    commentInputFieldState = state.commentInputFieldState.copy(
                        maxLength = state.commentInputFieldState.maxLength - commentPrefix.length
                    )
                )
            }
        }.launchIn(viewModelScope)

        combine(
            cardsRepository.getAuthedCards(),
            preferencesRepository.getSelectedSpServer()
        ) { cards, server ->
            cards.filter { it.server == server }
        }
            .onEach { cards ->
                val card = cards.find { it.id == args.cardId }
                    ?: cards.firstOrNull()
                    ?: return@onEach

                _state.update { state ->
                    state.copy(
                        sourceCards = cards,
                        selectedSourceCard = cards.indexOf(card)
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
            ),
            _state.observeInputField(
                inputField = { it.commentInputFieldState },
                validation = { commentValidator.validate(it) },
                updateState = { value -> _state.update { it.copy(commentInputFieldState = value) } }
            )
        ).onEach { isAllValid ->
            _state.update { it.copy(isTransferButtonEnabled = isAllValid) }
        }.launchIn(viewModelScope)
    }

    fun onAction(action: TransferByCardAction) {
        when (action) {
            TransferByCardAction.NavigateBack -> goBack()
            is TransferByCardAction.SelectSourceCard -> selectSourceCard(action.index)
            is TransferByCardAction.EditRecipient -> editRecipient()
            is TransferByCardAction.MakeTransfer -> makeTransfer()
            is TransferByCardAction.ChangeAmountValue -> changeAmountValue(action.value)
            is TransferByCardAction.ChangeCommentValue -> changeCommentValue(action.value)
            TransferByCardAction.ClearAmountFocus -> clearAmountFocus()
            TransferByCardAction.ClearCommentFocus -> clearCommentFocus()
        }
    }

    fun updateRecipient(cardNumber: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(loadingState = LoadingState.Loading)
            }

            _state.update { state ->
                state.copy(
                    recipient = state.recipient.copy(
                        number = cardNumber
                    )
                )
            }

            _state.update {
                it.copy(loadingState = LoadingState.Finished)
            }
        }
    }

    private fun makeTransfer() {
        viewModelScope.launch {
            _state.update { it.copy(loadingState = LoadingState.Loading) }

            val selectedCard = state.value.sourceCards[state.value.selectedSourceCard]
            val comment =
                "$commentPrefix${_state.value.commentInputFieldState.value.text.ifBlank { "Без комментария" }}"

            transferByCardUseCase(
                card = selectedCard,
                receiver = _state.value.recipient.number,
                amount = _state.value.amountInputFieldState.value.text,
                comment = comment
            ).onError {
                snackbarController.send(Snackbar(it.asUiText()))
            }.onSuccess {
                snackbarController.send(Snackbar(ResourceString(Res.string.transaction_succeed)))
                navEventController.navigate(TransferByCardNavEvent.NavigateBack)
            }

            recipientRepository.insertRecipient(
                recipientCard = emptyRecipientCard.copy(
                    server = preferencesRepository.getSelectedSpServer().first(),
                    number = _state.value.recipient.number
                )
            )

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

    private fun changeCommentValue(value: TextFieldValue) {
        val value = value
            .filterBy(InputFilters.LettersOrDigits.predicate)
            .cutOffAt(CoreConstants.MAX_COMMENT_LENGTH) ?: return

        _state.update { state ->
            state.copy(
                commentInputFieldState = state.commentInputFieldState.copy(
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

    private fun clearCommentFocus() {
        _state.validateInputField(
            inputField = { it.commentInputFieldState },
            validation = { commentValidator.validate(it) },
            updateState = { value -> _state.update { it.copy(commentInputFieldState = value) } }
        )
    }

    private fun goBack() {
        viewModelScope.launch {
            navEventController.navigate(TransferByCardNavEvent.NavigateBack)
        }
    }

    private fun editRecipient() {
        viewModelScope.launch {
            navEventController.navigate(TransferByCardNavEvent.NavigateToEditRecipient)
        }
    }

    private fun selectSourceCard(index: Int) {
        _state.update { it.copy(selectedSourceCard = index) }
    }
}