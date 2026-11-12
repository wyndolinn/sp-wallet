package com.wynndie.spwallet.sharedFeature.transfer.presentation.transferByCard

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wynndie.spwallet.sharedCore.Res
import com.wynndie.spwallet.sharedCore.domain.helpers.CoreConstants
import com.wynndie.spwallet.sharedCore.domain.helpers.emptyRecipientCard
import com.wynndie.spwallet.sharedCore.domain.outcome.onError
import com.wynndie.spwallet.sharedCore.domain.outcome.onSuccess
import com.wynndie.spwallet.sharedCore.domain.repositories.CardsRepository
import com.wynndie.spwallet.sharedCore.domain.repositories.PreferencesRepository
import com.wynndie.spwallet.sharedCore.domain.repositories.RecipientRepository
import com.wynndie.spwallet.sharedCore.domain.repositories.UserRepository
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
import com.wynndie.spwallet.sharedCore.presentation.extensions.trimSpaces
import com.wynndie.spwallet.sharedCore.presentation.extensions.validateInputField
import com.wynndie.spwallet.sharedCore.presentation.formatters.LoadingState
import com.wynndie.spwallet.sharedCore.presentation.formatters.asUiText
import com.wynndie.spwallet.sharedCore.presentation.models.InputFilters
import com.wynndie.spwallet.sharedCore.presentation.models.UiText.ResourceString
import com.wynndie.spwallet.sharedCore.transaction_succeed
import com.wynndie.spwallet.sharedFeature.transfer.domain.constants.TransferConstants
import com.wynndie.spwallet.sharedFeature.transfer.domain.useCases.TransferByCardUseCase
import com.wynndie.spwallet.sharedFeature.transfer.domain.validators.TransferCommentValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransferByCardViewModel(
    private val args: TransferByCardParams,
    private val userRepository: UserRepository,
    private val cardsRepository: CardsRepository,
    private val recipientRepository: RecipientRepository,
    private val preferencesRepository: PreferencesRepository,
    private val transferByCardUseCase: TransferByCardUseCase,
    private val amountValidator: AmountValidator,
    private val commentValidator: TransferCommentValidator,
    private val navEventController: NavEventController,
    private val snackbarController: SnackbarController,
) : ViewModel() {

    private val _state = MutableStateFlow(TransferByCardState())
    val state = _state.asStateFlow()


    init {
        observeUser()
        observeCards()
        observeInputValidation()
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
            _state.update { it.copy(loadingState = LoadingState.Loading) }

            _state.update { state ->
                state.copy(recipient = state.recipient.copy(number = cardNumber))
            }

            _state.update { it.copy(loadingState = LoadingState.Finished) }
        }
    }


    private fun makeTransfer() {
        viewModelScope.launch {
            _state.update { it.copy(loadingState = LoadingState.Loading) }
            val currentState = _state.value

            val commentValue = currentState.commentInputFieldState.value.text
            val prefix = currentState.commentPrefix
            val comment = "$prefix${commentValue.ifBlank { "Без комментария" }}"

            transferByCardUseCase(
                card = currentState.sourceCards[currentState.selectedSourceCardIndex],
                receiver = currentState.recipient.number,
                amount = currentState.amountInputFieldState.value.text,
                comment = comment,
            ).onSuccess {
                recipientRepository.insertRecipient(
                    recipientCard = emptyRecipientCard.copy(
                        server = preferencesRepository.getSelectedServer().first(),
                        number = currentState.recipient.number,
                    ),
                )

                snackbarController.send(ResourceString(Res.string.transaction_succeed))
                navEventController.navigate(TransferByCardNavEvent.NavigateBack)
            }.onError { snackbarController.send(it.asUiText()) }

            _state.update { it.copy(loadingState = LoadingState.Finished) }
        }
    }

    private fun changeAmountValue(value: TextFieldValue) {
        _state.update { state ->
            val filteredValue = value
                .filter(InputFilters.Numbers)
                .dropWhile('0') { it.length > 1 }
                .cutOffAt(CoreConstants.MAX_BALANCE_LENGTH) ?: return@update state

            state.copy(amountInputFieldState = state.amountInputFieldState.copy(value = filteredValue))
        }
    }

    private fun changeCommentValue(value: TextFieldValue) {
        _state.update { state ->
            val filteredValue = value
                .filter(InputFilters.Text)
                .trimSpaces()
                .cutOffAt(TransferConstants.MAX_COMMENT_LENGTH) ?: return@update state

            state.copy(commentInputFieldState = state.commentInputFieldState.copy(value = filteredValue))
        }
    }

    private fun clearAmountFocus() {
        val value = _state.value.amountInputFieldState.value.text
        val maxValue = _state.value.sourceCards[_state.value.selectedSourceCardIndex].balance

        _state.validateInputField(
            inputField = { it.amountInputFieldState },
            validator = {
                val validationValues = ValidationValues(value = value, maxValue = maxValue)
                amountValidator.validate(validationValues)
            },
        ) { copy(amountInputFieldState = it) }
    }

    private fun clearCommentFocus() {
        _state.validateInputField(
            inputField = { it.commentInputFieldState },
            validator = commentValidator::validate,
        ) { copy(commentInputFieldState = it) }
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
        _state.update { it.copy(selectedSourceCardIndex = index) }
    }


    private fun observeInputValidation() {
        combine(
            _state.select { it.amountInputFieldState.value },
            _state.select { it.sourceCards },
            _state.select { it.selectedSourceCardIndex }
        ) { _, cards, cardIndex ->
            if (cards.isEmpty()) return@combine
            val maxValue = cards[cardIndex].balance
            _state.resetValidationState(
                inputField = { it.amountInputFieldState },
                validator = { amountValidator.validate(it.copy(maxValue = maxValue)) }
            ) { copy(amountInputFieldState = it) }
        }.launchIn(viewModelScope)

        _state
            .select { it.commentInputFieldState.value }
            .onEach {
                _state.resetValidationState(
                    inputField = { it.commentInputFieldState },
                    validator = commentValidator::validate
                ) { copy(commentInputFieldState = it) }
            }.launchIn(viewModelScope)

        _state.observeValidStates(
            _state.select { it.amountInputFieldState.hasError },
            _state.select { it.commentInputFieldState.hasError },
        ) { copy(isTransferButtonEnabled = it) }.launchIn(viewModelScope)
    }

    private fun observeCards() {
        combine(
            cardsRepository.getAuthedCards(),
            preferencesRepository.getSelectedServer(),
        ) { authedCards, server ->
            val sourceCards = authedCards.filter { it.server == server }
            val selectedCard = authedCards.find { it.id == args.sourceCardId }
            _state.update { state ->
                state.copy(sourceCards = selectedCard?.let { listOf(it) } ?: sourceCards)
            }
        }.launchIn(viewModelScope)
    }

    private fun observeUser() {
        userRepository.getAuthedUsers().onEach { users ->
            val user = users.firstOrNull() ?: return@onEach
            val prefix = "${user.name}: "
            _state.update { state ->
                state.copy(
                    user = user,
                    commentPrefix = prefix,
                    commentInputFieldState = state.commentInputFieldState.copy(
                        maxLength = state.commentInputFieldState.maxLength - prefix.length,
                    ),
                )
            }
        }.launchIn(viewModelScope)
    }
}