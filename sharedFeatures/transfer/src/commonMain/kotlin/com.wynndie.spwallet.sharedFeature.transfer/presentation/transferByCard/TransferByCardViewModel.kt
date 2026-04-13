package com.wynndie.spwallet.sharedFeature.transfer.presentation.transferByCard

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
import com.wynndie.spwallet.sharedCore.presentation.states.LoadingState
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
    private val args: TransferByCardViewModelArgs,
    private val recipientRepository: RecipientRepository,
    private val preferencesRepository: PreferencesRepository,
    private val transferByCardUseCase: TransferByCardUseCase,
    private val transferAmountValidator: BalanceValidator,
    private val commentValidator: TransferCommentValidator,
) : ViewModel() {

    private val _state = MutableStateFlow(TransferByCardState())
    val state = _state.asStateFlow()

    private var commentPrefix = ""
    private var maxValue = 0L

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
                        cards = cards,
                        carouselPage = cards.indexOf(card)
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
                    val comment = "$commentPrefix${action.comment.ifBlank { "Без комментария" }}"

                    transferByCardUseCase(
                        card = selectedCard,
                        receiverCardNumber = action.cardNumber,
                        amount = action.transferAmount,
                        comment = comment
                    ).onError {
                        OverlayController.send(Snackbar(it.asUiText()))
                    }.onSuccess {
                        OverlayController.send(
                            Snackbar(ResourceString(Res.string.transaction_succeed))
                        )
                        NavController.navigate(TransferByCardNavEvent.OnClickBack)
                    }

                    recipientRepository.insertRecipient(
                        recipientCard = emptyRecipientCard.copy(
                            server = preferencesRepository.getSelectedSpServer().first(),
                            number = action.cardNumber
                        )
                    )

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

            TransferByCardAction.OnToggleCommentFocus -> {
                _state.validateInputField(
                    inputField = { it.commentInputFieldState },
                    validation = { commentValidator.validate(it) },
                    updateState = { value -> _state.update { it.copy(commentInputFieldState = value) } }
                )
            }

            TransferByCardAction.OnToggleTransferAmountFocus -> {
                _state.validateInputField(
                    inputField = { it.amountInputFieldState },
                    validation = {
                        val validationValues = BalanceValidationValues(
                            value = _state.value.amountInputFieldState.value.text,
                            maxValue = _state.value.cards[_state.value.carouselPage].balance
                        )
                        transferAmountValidator.validate(validationValues)
                    },
                    updateState = { value -> _state.update { it.copy(amountInputFieldState = value) } }
                )
            }
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
}