package com.wynndie.spwallet.sharedFeature.home.presentation.screens.home

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wynndie.spwallet.sharedCore.domain.error.onError
import com.wynndie.spwallet.sharedCore.domain.error.onSuccess
import com.wynndie.spwallet.sharedCore.domain.repositories.CardsRepository
import com.wynndie.spwallet.sharedCore.domain.repositories.UserRepository
import com.wynndie.spwallet.sharedCore.presentation.controllers.navigation.NavController
import com.wynndie.spwallet.sharedCore.presentation.controllers.overlay.OverlayController
import com.wynndie.spwallet.sharedCore.presentation.controllers.overlay.OverlayType
import com.wynndie.spwallet.sharedCore.presentation.extensions.asUiText
import com.wynndie.spwallet.sharedCore.presentation.states.LoadingState
import com.wynndie.spwallet.sharedCore.presentation.models.cards.AuthedCardUi
import com.wynndie.spwallet.sharedCore.presentation.models.cards.CustomCardUi
import com.wynndie.spwallet.sharedCore.presentation.models.cards.UnauthedCardUi
import com.wynndie.spwallet.sharedCore.presentation.formatters.displayableValue.BlocksDisplayableValue
import com.wynndie.spwallet.sharedCore.presentation.formatters.input.InputFilterOptions
import com.wynndie.spwallet.sharedCore.presentation.formatters.input.cutOffAt
import com.wynndie.spwallet.sharedCore.presentation.formatters.input.filterBy
import com.wynndie.spwallet.sharedFeature.home.domain.useCases.AuthCardUseCase
import com.wynndie.spwallet.sharedFeature.home.domain.usecase.DeleteAuthedCardUseCase
import com.wynndie.spwallet.sharedFeature.home.domain.usecase.SyncWithRemoteUseCase
import com.wynndie.spwallet.sharedFeature.home.domain.validator.TokenValidator
import com.wynndie.spwallet.sharedFeature.home.domain.validator.UuidValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    userRepository: UserRepository,
    cardsRepository: CardsRepository,
    private val syncWithRemoteUseCase: SyncWithRemoteUseCase,
    private val deleteAuthedCardUseCase: DeleteAuthedCardUseCase,
    private val authCardUseCase: AuthCardUseCase,
    private val uuidValidator: UuidValidator,
    private val tokenValidator: TokenValidator
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            syncWithRemote()
        }

        userRepository.getAuthedUsers().onEach { users ->
            users.firstOrNull()?.let { user ->
                _state.update { it.copy(authedUser = user) }
            }
        }.launchIn(viewModelScope)

        cardsRepository.getAuthedCards().onEach { cards ->
            _state.update { state ->
                state.copy(authedCards = cards.map { AuthedCardUi.of(it) })
            }
            updateBalance()
        }.launchIn(viewModelScope)

        cardsRepository.getUnauthedCards().onEach { cards ->
            _state.update { state ->
                state.copy(unauthedCards = cards.map { UnauthedCardUi.of(it) })
            }
        }.launchIn(viewModelScope)

        cardsRepository.getCustomCards().onEach { cards ->
            _state.update { state ->
                state.copy(customCards = cards.map { CustomCardUi.of(it) })
            }
            updateBalance()
        }.launchIn(viewModelScope)
    }

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.OnRefresh -> {
                viewModelScope.launch {
                    closeAllDialogs()
                    syncWithRemote()
                }
            }


            is HomeAction.OnSwipeCarousel -> {
                _state.update { state ->
                    state.copy(carouselPage = action.index)
                }
            }


            HomeAction.OnToggleAuthCardSheet -> {
                _state.update { state ->
                    state.copy(
                        isAuthCardSheetVisible = !state.isAuthCardSheetVisible,
                        isAuthedCardSheetVisible = false,
                        isDeactivateCardDialogVisible = false
                    )
                }
            }

            HomeAction.OnToggleAuthedCardSheet -> {
                _state.update { state ->
                    state.copy(
                        isAuthCardSheetVisible = false,
                        isAuthedCardSheetVisible = !state.isAuthedCardSheetVisible,
                        isDeactivateCardDialogVisible = false
                    )
                }
            }

            HomeAction.OnToggleDeleteCardDialog -> {
                _state.update { state ->
                    state.copy(
                        isAuthCardSheetVisible = false,
                        isAuthedCardSheetVisible = true,
                        isDeactivateCardDialogVisible = !state.isDeactivateCardDialogVisible
                    )
                }
            }


            is HomeAction.OnClickAuthCard -> {
                viewModelScope.launch {

                    _state.update {
                        it.copy(authLoadingState = LoadingState.Loading)
                    }

                    val validationResults = listOf(
                        isCardIdValid(action.id),
                        isCardTokenValid(action.token)
                    )

                    if (!validationResults.any { isValid -> !isValid }) {
                        authCardUseCase(id = action.id, token = action.token)
                            .onError {
                                OverlayController.send(OverlayType.Snackbar(it.asUiText()))
                            }
                            .onSuccess {
                                syncWithRemoteUseCase()
                                _state.update { state ->
                                    state.copy(
                                        idInputField = state.idInputField.copy(
                                            value = TextFieldValue("")
                                        ),
                                        tokenInputField = state.tokenInputField.copy(
                                            value = TextFieldValue("")
                                        ),
                                        isAuthCardSheetVisible = false
                                    )
                                }
                            }
                    }

                    _state.update {
                        it.copy(authLoadingState = LoadingState.Finished)
                    }
                }
            }

            is HomeAction.OnClickDeactivateCard -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(screenLoadingState = LoadingState.Loading)
                    }

                    deleteAuthedCardUseCase(action.card.toDomain())
                    closeAllDialogs()

                    _state.update {
                        it.copy(screenLoadingState = LoadingState.Finished)
                    }
                }
            }

            is HomeAction.OnClickTransferByCard -> {
                viewModelScope.launch {
                    NavController.navigate(HomeNavEvent.OnClickTransferByCard(action.cardId))
                    closeAllDialogs()
                }
            }


            is HomeAction.OnClickCustomCard -> {
                viewModelScope.launch {
                    NavController.navigate(HomeNavEvent.OnClickCustomCard(action.cardId))
                    closeAllDialogs()
                }
            }

            is HomeAction.OnClickAuthedCard -> {
                closeAllDialogs()
                val card = state.value.authedCards.find { it.id == action.cardId } ?: return
                val cardIndex = state.value.authedCards.indexOf(card)
                _state.update { state ->
                    state.copy(
                        isAuthedCardSheetVisible = true,
                        carouselPage = cardIndex
                    )
                }
            }

            is HomeAction.OnClickUnauthedCard -> {
                closeAllDialogs()
                val cardIndex = state.value.unauthedCards.indexOfFirst { it.id == action.cardId }
                _state.update { state ->
                    state.copy(
                        isAuthCardSheetVisible = true,
                        carouselPage = cardIndex
                    )
                }
            }


            is HomeAction.OnChangeCardIdValue -> {
                val value = action.value
                    .filterBy(InputFilterOptions.Structured.Uuid.predicate)
                    .cutOffAt(state.value.idInputField.maxLength) ?: return

                _state.update { state ->
                    state.copy(
                        idInputField = state.idInputField.copy(
                            value = value
                        )
                    )
                }
            }

            is HomeAction.OnChangeCardTokenValue -> {
                val value = action.value
                    .filterBy(InputFilterOptions.Structured.Base64.predicate)
                    .cutOffAt(state.value.tokenInputField.maxLength) ?: return

                _state.update { state ->
                    state.copy(
                        tokenInputField = state.tokenInputField.copy(
                            value = value
                        )
                    )
                }
            }
        }
    }

    private suspend fun syncWithRemote() {
        _state.update {
            it.copy(screenLoadingState = LoadingState.Loading)
        }

        syncWithRemoteUseCase()
            .onError { error ->
                OverlayController.send(OverlayType.Snackbar(error.asUiText()))
            }

        _state.update {
            it.copy(screenLoadingState = LoadingState.Finished)
        }
    }

    private fun updateBalance() {
        val authedCardsBalance = state.value.authedCards.sumOf { it.balance.value }
        val customCardsBalance = state.value.customCards.sumOf { it.balance.value }
        val totalBalance = authedCardsBalance + customCardsBalance
        _state.update {
            it.copy(totalBalance = BlocksDisplayableValue.of(totalBalance))
        }
    }

    private fun closeAllDialogs() {
        _state.update {
            it.copy(
                isAuthCardSheetVisible = false,
                isAuthedCardSheetVisible = false,
                isDeactivateCardDialogVisible = false
            )
        }
    }

    private fun isCardTokenValid(token: String): Boolean {
        val (isValid, error) = tokenValidator.validate(token)
        _state.update { state ->
            state.copy(
                tokenInputField = state.tokenInputField.copy(
                    supportingText = error?.asUiText(),
                    hasError = !isValid
                )
            )
        }
        return isValid
    }

    private fun isCardIdValid(id: String): Boolean {
        val (isValid, error) = uuidValidator.validate(id)
        _state.update { state ->
            state.copy(
                idInputField = state.idInputField.copy(
                    supportingText = error?.asUiText(),
                    hasError = !isValid
                )
            )
        }
        return isValid
    }
}