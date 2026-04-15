package com.wynndie.spwallet.sharedFeature.home.presentation

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wynndie.spwallet.sharedCore.domain.models.SpServers
import com.wynndie.spwallet.sharedCore.domain.models.cards.AuthedCard
import com.wynndie.spwallet.sharedCore.domain.models.cards.CustomCard
import com.wynndie.spwallet.sharedCore.domain.models.cards.UnauthedCard
import com.wynndie.spwallet.sharedCore.domain.outcome.getOrElse
import com.wynndie.spwallet.sharedCore.domain.outcome.onError
import com.wynndie.spwallet.sharedCore.domain.repositories.CardsRepository
import com.wynndie.spwallet.sharedCore.domain.repositories.PreferencesRepository
import com.wynndie.spwallet.sharedCore.domain.repositories.UserRepository
import com.wynndie.spwallet.sharedCore.presentation.controllers.navigation.NavEventController
import com.wynndie.spwallet.sharedCore.presentation.controllers.overlay.Snackbar
import com.wynndie.spwallet.sharedCore.presentation.controllers.overlay.SnackbarController
import com.wynndie.spwallet.sharedCore.presentation.extensions.asUiText
import com.wynndie.spwallet.sharedCore.presentation.extensions.observeInputField
import com.wynndie.spwallet.sharedCore.presentation.extensions.observeValidationStates
import com.wynndie.spwallet.sharedCore.presentation.extensions.validateInputField
import com.wynndie.spwallet.sharedCore.presentation.formatters.LoadingState
import com.wynndie.spwallet.sharedCore.presentation.formatters.input.InputFilters
import com.wynndie.spwallet.sharedCore.presentation.formatters.input.cutOffAt
import com.wynndie.spwallet.sharedCore.presentation.formatters.input.filterBy
import com.wynndie.spwallet.sharedFeature.home.domain.useCases.AuthCardUseCase
import com.wynndie.spwallet.sharedFeature.home.domain.useCases.DeleteAuthedCardUseCase
import com.wynndie.spwallet.sharedFeature.home.domain.useCases.SyncWithRemoteUseCase
import com.wynndie.spwallet.sharedFeature.home.domain.validators.TokenValidator
import com.wynndie.spwallet.sharedFeature.home.domain.validators.UuidValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    userRepository: UserRepository,
    cardsRepository: CardsRepository,
    private val preferencesRepository: PreferencesRepository,
    private val syncWithRemoteUseCase: SyncWithRemoteUseCase,
    private val deleteAuthedCardUseCase: DeleteAuthedCardUseCase,
    private val authCardUseCase: AuthCardUseCase,
    private val uuidValidator: UuidValidator,
    private val tokenValidator: TokenValidator,
    private val navEventController: NavEventController,
    private val snackbarController: SnackbarController
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        syncWithRemote()


        preferencesRepository.getSelectedSpServer().onEach { server ->
            _state.update { state ->
                state.copy(selectedServer = server)
            }
        }.launchIn(viewModelScope)

        userRepository.getAuthedUsers().onEach { users ->
            users.firstOrNull()?.let { user ->
                _state.update { it.copy(authedUser = user) }
            }
        }.launchIn(viewModelScope)

        combine(
            cardsRepository.getAuthedCards(),
            cardsRepository.getUnauthedCards(),
            cardsRepository.getCustomCards(),
            preferencesRepository.getSelectedSpServer()
        ) { authedCards, unauthedCard, customCards, selectedSever ->
            HomeCardsData(
                authedCards = authedCards.filter { it.server == selectedSever },
                unauthedCards = unauthedCard.filter { it.server == selectedSever },
                customCards = customCards.filter { it.server == selectedSever }
            )
        }.onEach { data ->
            _state.update { state ->
                state.copy(
                    authedCards = data.authedCards,
                    unauthedCards = data.unauthedCards,
                    customCards = data.customCards
                )
            }
            updateBalance()
        }.launchIn(viewModelScope)


        observeValidationStates(
            _state.observeInputField(
                inputField = { it.idInputFieldState },
                validation = { uuidValidator.validate(it) },
                updateState = { _state.update { state -> state.copy(idInputFieldState = it) } }
            ),
            _state.observeInputField(
                inputField = { it.tokenInputFieldState },
                validation = { tokenValidator.validate(it) },
                updateState = { _state.update { state -> state.copy(tokenInputFieldState = it) } }
            )
        ).onEach { isAllValid ->
            _state.update { it.copy(isAuthButtonEnabled = isAllValid) }
        }.launchIn(viewModelScope)
    }


    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.Refresh -> syncWithRemote()
            is HomeAction.SelectServer -> selectServer(action.server)
            is HomeAction.ToggleAuthCardSheet -> toggleAuthCardSheet(action.open)
            is HomeAction.ToggleAuthedCardSheet -> toggleAuthedCardSheet(action.open)
            is HomeAction.ToggleDeleteCardDialog -> toggleDeleteCardDialog(action.open)
            is HomeAction.TransferBetweenCards -> transferBetweenCard(action.id)
            is HomeAction.TransferByCard -> transferByCard(action.id)
            is HomeAction.AuthCard -> authCard(action.id, action.token)
            is HomeAction.DeactivateCard -> deactivateCard(action.card)
            is HomeAction.SelectAuthedCard -> selectAuthedCard(action.id)
            is HomeAction.SelectUnauthedCard -> selectUnauthedCard(action.id)
            is HomeAction.SelectCustomCard -> selectCustomCard(action.id)
            is HomeAction.ChangeCardIdValue -> changeIdValue(action.value)
            is HomeAction.ChangeTokenValue -> changeTokenValue(action.value)
            HomeAction.ClearIdFocus -> clearIdFocus()
            HomeAction.ClearCardTokenFocus -> clearTokenFocus()
        }
    }

    private fun syncWithRemote() {
        closeOverlays()
        viewModelScope.launch {
            _state.update {
                it.copy(screenLoadingState = LoadingState.Loading)
            }

            syncWithRemoteUseCase()
                .onError { error ->
                    snackbarController.send(Snackbar(error.asUiText()))
                }

            _state.update {
                it.copy(screenLoadingState = LoadingState.Finished)
            }
        }
    }

    private fun authCard(id: String, token: String) {
        viewModelScope.launch {

            _state.update {
                it.copy(authLoadingState = LoadingState.Loading)
            }

            authCardUseCase(
                server = _state.value.selectedServer,
                id = id,
                token = token
            ).getOrElse { error ->
                snackbarController.send(Snackbar(error.asUiText()))
                _state.update {
                    it.copy(authLoadingState = LoadingState.Finished)
                }
                return@launch
            }

            syncWithRemoteUseCase()

            _state.update { state ->
                state.copy(
                    idInputFieldState = state.idInputFieldState.copy(
                        value = TextFieldValue("")
                    ),
                    tokenInputFieldState = state.tokenInputFieldState.copy(
                        value = TextFieldValue("")
                    ),
                    isAuthCardSheetVisible = false
                )
            }

            _state.update {
                it.copy(authLoadingState = LoadingState.Finished)
            }
        }
    }

    private fun deactivateCard(card: AuthedCard) {
        viewModelScope.launch {
            _state.update {
                it.copy(screenLoadingState = LoadingState.Loading)
            }

            deleteAuthedCardUseCase(card)
            closeOverlays()

            _state.update {
                it.copy(screenLoadingState = LoadingState.Finished)
            }
        }
    }

    private fun changeIdValue(value: TextFieldValue) {
        val value = value
            .filterBy(InputFilters.Uuid.predicate)
            .cutOffAt(state.value.idInputFieldState.maxLength) ?: return

        _state.update { state ->
            state.copy(
                idInputFieldState = state.idInputFieldState.copy(
                    value = value
                )
            )
        }
    }

    private fun changeTokenValue(value: TextFieldValue) {
        val value = value
            .filterBy(InputFilters.Base64.predicate)
            .cutOffAt(state.value.tokenInputFieldState.maxLength) ?: return

        _state.update { state ->
            state.copy(
                tokenInputFieldState = state.tokenInputFieldState.copy(
                    value = value
                )
            )
        }
    }

    private fun clearIdFocus() {
        _state.validateInputField(
            inputField = { it.idInputFieldState },
            validation = { uuidValidator.validate(it) },
            updateState = { _state.update { state -> state.copy(idInputFieldState = it) } }
        )
    }

    private fun clearTokenFocus() {
        _state.validateInputField(
            inputField = { it.tokenInputFieldState },
            validation = { tokenValidator.validate(it) },
            updateState = { _state.update { state -> state.copy(tokenInputFieldState = it) } }
        )
    }

    private fun selectServer(server: SpServers) {
        viewModelScope.launch {
            preferencesRepository.setSelectedSpServer(server)
        }
    }

    private fun toggleAuthCardSheet(open: Boolean) {
        _state.update { state ->
            state.copy(
                isAuthCardSheetVisible = open,
                isAuthedCardSheetVisible = false,
                isDeactivateCardDialogVisible = false
            )
        }
    }

    private fun toggleAuthedCardSheet(open: Boolean) {
        _state.update { state ->
            state.copy(
                isAuthCardSheetVisible = false,
                isAuthedCardSheetVisible = open,
                isDeactivateCardDialogVisible = false
            )
        }
    }

    private fun toggleDeleteCardDialog(open: Boolean) {
        _state.update { state ->
            state.copy(
                isAuthCardSheetVisible = false,
                isAuthedCardSheetVisible = true,
                isDeactivateCardDialogVisible = open
            )
        }
    }

    private fun transferBetweenCard(id: String) {
        viewModelScope.launch {
            navEventController.navigate(HomeNavEvent.NavigateToTransferBetweenCards(id))
        }
    }

    private fun transferByCard(id: String) {
        viewModelScope.launch {
            navEventController.navigate(HomeNavEvent.NavigateToTransferByCard(id))
            closeOverlays()
        }
    }

    private fun selectAuthedCard(id: String) {
        closeOverlays()
        val card = state.value.authedCards.find { it.id == id } ?: return
        val cardIndex = state.value.authedCards.indexOf(card)
        _state.update { state ->
            state.copy(
                isAuthedCardSheetVisible = true,
                carouselPage = cardIndex
            )
        }
    }

    private fun selectUnauthedCard(id: String) {
        closeOverlays()
        val cardIndex = state.value.unauthedCards.indexOfFirst { it.id == id }
        _state.update { state ->
            state.copy(
                isAuthCardSheetVisible = true,
                carouselPage = cardIndex
            )
        }
    }

    private fun selectCustomCard(id: String) {
        viewModelScope.launch {
            navEventController.navigate(HomeNavEvent.NavigateToCustomCard(id))
            closeOverlays()
        }
    }

    private fun updateBalance() {
        val authedCardsBalance = state.value.authedCards.sumOf { it.balance }
        val customCardsBalance = state.value.customCards.sumOf { it.balance }
        val totalBalance = authedCardsBalance + customCardsBalance
        _state.update { it.copy(totalBalance = totalBalance) }
    }

    private fun closeOverlays() {
        _state.update {
            it.copy(
                isAuthCardSheetVisible = false,
                isAuthedCardSheetVisible = false,
                isDeactivateCardDialogVisible = false
            )
        }
    }

    companion object {
        private data class HomeCardsData(
            val authedCards: List<AuthedCard>,
            val unauthedCards: List<UnauthedCard>,
            val customCards: List<CustomCard>
        )
    }
}