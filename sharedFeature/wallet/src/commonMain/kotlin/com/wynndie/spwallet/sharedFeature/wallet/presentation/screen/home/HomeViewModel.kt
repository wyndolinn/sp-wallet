package com.wynndie.spwallet.sharedFeature.wallet.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wynndie.spwallet.sharedCore.domain.error.getOrNull
import com.wynndie.spwallet.sharedCore.domain.error.onError
import com.wynndie.spwallet.sharedCore.domain.error.onSuccess
import com.wynndie.spwallet.sharedCore.presentation.controller.dialog.Dialog
import com.wynndie.spwallet.sharedCore.presentation.controller.dialog.DialogController
import com.wynndie.spwallet.sharedCore.presentation.formatter.InputFormatter
import com.wynndie.spwallet.sharedCore.presentation.formatter.StructuredFilter
import com.wynndie.spwallet.sharedCore.presentation.model.LoadingState
import com.wynndie.spwallet.sharedCore.presentation.text.UiText
import com.wynndie.spwallet.sharedCore.presentation.text.asUiText
import com.wynndie.spwallet.sharedFeature.wallet.domain.constants.Constants
import com.wynndie.spwallet.sharedFeature.wallet.domain.repository.WalletRepository
import com.wynndie.spwallet.sharedFeature.wallet.domain.usecase.AuthCardUseCase
import com.wynndie.spwallet.sharedFeature.wallet.domain.usecase.SyncWithRemoteUseCase
import com.wynndie.spwallet.sharedFeature.wallet.domain.validator.TokenValidator
import com.wynndie.spwallet.sharedFeature.wallet.domain.validator.UuidValidator
import com.wynndie.spwallet.sharedFeature.wallet.presentation.model.BlocksDisplayableValue
import com.wynndie.spwallet.sharedFeature.wallet.presentation.model.UiAuthedCard
import com.wynndie.spwallet.sharedFeature.wallet.presentation.model.UiCashCard
import com.wynndie.spwallet.sharedFeature.wallet.presentation.model.UiUnauthedCard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val walletRepository: WalletRepository,
    private val syncWithRemoteUseCase: SyncWithRemoteUseCase,
    private val authCardUseCase: AuthCardUseCase,
    private val uuidValidator: UuidValidator,
    private val tokenValidator: TokenValidator
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {

            syncWithRemote()

            launch {
                walletRepository.getAuthedUsers().onEach { users ->
                    users.firstOrNull()?.let { user ->
                        _state.update { it.copy(authedUser = user) }
                    }
                }.launchIn(this)
            }

            launch {
                walletRepository.getAuthedCards().onEach { cards ->
                    _state.update { state ->
                        state.copy(authedCards = cards.map { UiAuthedCard.of(it) })
                    }
                    updateBalance()
                }.launchIn(this)
            }

            launch {
                walletRepository.getUnauthedCards().onEach { cards ->
                    _state.update { state ->
                        state.copy(unauthedCards = cards.map { UiUnauthedCard.of(it) })
                    }
                }.launchIn(this)
            }

            launch {
                walletRepository.getCashCards().onEach { cards ->
                    _state.update { state ->
                        state.copy(cashCards = cards.map { UiCashCard.of(it) })
                    }
                    updateBalance()
                }.launchIn(this)
            }
        }
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
                        isAuthedCardSheetVisible = !state.isAuthedCardSheetVisible,
                        isAuthCardSheetVisible = false,
                        isDeactivateCardDialogVisible = false
                    )
                }
            }

            HomeAction.OnToggleDeleteCardDialog -> {
                _state.update { state ->
                    state.copy(
                        isDeactivateCardDialogVisible = !state.isDeactivateCardDialogVisible,
                        isAuthCardSheetVisible = false,
                        isAuthedCardSheetVisible = true
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
                                DialogController.send(Dialog.Toast(it.asUiText()))
                            }
                            .onSuccess {
                                _state.update {
                                    it.copy(isAuthCardSheetVisible = false)
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
                    walletRepository.deleteAuthedCard(action.card.toDomain())
                    closeAllDialogs()
                }
            }

            is HomeAction.OnClickTransferByCard -> {
                closeAllDialogs()
                action.navigate(action.card.id)
            }


            is HomeAction.OnClickCashCard -> {
                closeAllDialogs()
                action.navigate(action.card.id)
            }

            is HomeAction.OnClickAuthedCard -> {
                closeAllDialogs()
                val card = state.value.authedCards.find { it.id == action.card.id } ?: return
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
                val cardIndex = state.value.unauthedCards.indexOf(action.card)
                _state.update { state ->
                    state.copy(
                        isAuthCardSheetVisible = true,
                        carouselPage = cardIndex
                    )
                }
            }


            is HomeAction.OnChangeCardIdValue -> {
                val inputFormatter = InputFormatter(action.value)
                    .filterBy(StructuredFilter.UUID.predicate)
                    .cutOffAt(Constants.UUID_LENGTH) ?: return

                _state.update { state ->
                    state.copy(
                        idInputField = state.idInputField.copy(
                            value = inputFormatter.value
                        )
                    )
                }
            }

            is HomeAction.OnChangeCardTokenValue -> {
                val inputFormatter = InputFormatter(action.value)
                    .filterBy(StructuredFilter.BASE64.predicate)
                    .cutOffAt(Constants.TOKEN_LENGTH) ?: return

                _state.update { state ->
                    state.copy(
                        tokenInputField = state.tokenInputField.copy(
                            value = inputFormatter.value
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
                DialogController.send(Dialog.Toast(error.asUiText()))
            }

        _state.update {
            it.copy(screenLoadingState = LoadingState.Finished)
        }
    }

    private fun updateBalance() {
        val authedCardsBalance = state.value.authedCards.sumOf { it.balance.value }
        val cashCardsBalance = state.value.cashCards.sumOf { it.balance.value }
        val totalBalance = authedCardsBalance + cashCardsBalance
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
        return tokenValidator.validate(token)
            .onError { error ->
                _state.update { state ->
                    state.copy(
                        tokenInputField = state.tokenInputField.copy(
                            supportingText = error.asUiText(),
                            isError = true
                        )
                    )
                }
            }
            .onSuccess {
                _state.update { state ->
                    state.copy(
                        tokenInputField = state.tokenInputField.copy(
                            supportingText = UiText.DynamicString(""),
                            isError = false
                        )
                    )
                }
            }
            .getOrNull() ?: false
    }

    private fun isCardIdValid(id: String): Boolean {
        return uuidValidator.validate(id)
            .onError { error ->
                _state.update { state ->
                    state.copy(
                        idInputField = state.idInputField.copy(
                            supportingText = error.asUiText(),
                            isError = true
                        )
                    )
                }
            }
            .onSuccess {
                _state.update { state ->
                    state.copy(
                        idInputField = state.idInputField.copy(
                            supportingText = UiText.DynamicString(""),
                            isError = false
                        )
                    )
                }
            }
            .getOrNull() ?: false
    }
}