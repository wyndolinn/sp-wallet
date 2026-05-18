package com.wynndie.spwallet.sharedFeature.home.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wynndie.spwallet.sharedCore.Res
import com.wynndie.spwallet.sharedCore.domain.models.SpServers
import com.wynndie.spwallet.sharedCore.domain.models.cards.AuthedCard
import com.wynndie.spwallet.sharedCore.domain.models.cards.CustomCard
import com.wynndie.spwallet.sharedCore.domain.models.cards.UnauthedCard
import com.wynndie.spwallet.sharedCore.domain.outcome.onError
import com.wynndie.spwallet.sharedCore.domain.repositories.CardsRepository
import com.wynndie.spwallet.sharedCore.domain.repositories.PreferencesRepository
import com.wynndie.spwallet.sharedCore.domain.repositories.UserRepository
import com.wynndie.spwallet.sharedCore.not_enough_cards
import com.wynndie.spwallet.sharedCore.presentation.controllers.navigation.NavEventController
import com.wynndie.spwallet.sharedCore.presentation.controllers.overlay.Snackbar
import com.wynndie.spwallet.sharedCore.presentation.controllers.overlay.SnackbarController
import com.wynndie.spwallet.sharedCore.presentation.extensions.asUiText
import com.wynndie.spwallet.sharedCore.presentation.formatters.LoadingState
import com.wynndie.spwallet.sharedCore.presentation.formatters.UiText
import com.wynndie.spwallet.sharedCore.server_changed
import com.wynndie.spwallet.sharedFeature.home.domain.useCases.DeleteAuthedCardUseCase
import com.wynndie.spwallet.sharedFeature.home.domain.useCases.SyncWithRemoteUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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
    private val navEventController: NavEventController,
    private val snackbarController: SnackbarController
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private var loadingJob: Job? = null

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
            delay(150)
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
    }


    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.Refresh -> syncWithRemote()
            is HomeAction.SelectServer -> selectServer(action.server)
            is HomeAction.AuthCard -> authCard()
            is HomeAction.ToggleAuthedCardSheet -> toggleAuthedCardSheet(action.open)
            is HomeAction.ToggleDeleteCardDialog -> toggleDeleteCardDialog(action.open)
            is HomeAction.TransferBetweenCards -> transferBetweenCard(action.id)
            is HomeAction.TransferByCard -> transferByCard(action.id)
            is HomeAction.DeactivateCard -> deactivateCard(action.card)
            is HomeAction.SelectAuthedCard -> selectAuthedCard(action.id)
            is HomeAction.SelectUnauthedCard -> selectUnauthedCard(action.id)
            is HomeAction.SelectCustomCard -> selectCustomCard(action.id)
        }
    }

    private fun syncWithRemote() {
        closeOverlays()
        loadingJob?.cancel()
        loadingJob = viewModelScope.launch {
            _state.update { it.copy(screenLoadingState = LoadingState.Loading) }

            syncWithRemoteUseCase().onError { error ->
                snackbarController.send(Snackbar(error.asUiText()))
            }

            _state.update {
                it.copy(screenLoadingState = LoadingState.Finished)
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


    private fun selectServer(server: SpServers) {
        viewModelScope.launch {
            preferencesRepository.setSelectedSpServer(server)
            snackbarController.send(
                Snackbar(UiText.ResourceString(Res.string.server_changed, server.label))
            )
        }
    }

    private fun authCard() {
        viewModelScope.launch {
            navEventController.navigate(HomeNavEvent.NavigateToAuthCard)
        }
    }

    private fun toggleAuthedCardSheet(open: Boolean) {
        _state.update { state ->
            state.copy(
                isAuthedCardSheetVisible = open,
                isDeactivateCardDialogVisible = false
            )
        }
    }

    private fun toggleDeleteCardDialog(open: Boolean) {
        _state.update { state ->
            state.copy(
                isAuthedCardSheetVisible = true,
                isDeactivateCardDialogVisible = open
            )
        }
    }

    private fun transferBetweenCard(id: String) {
        viewModelScope.launch {
            val authedCardsSize = _state.value.authedCards.size
            val unauthedCardsSize = _state.value.unauthedCards.size
            if (authedCardsSize + unauthedCardsSize <= 1) {
                snackbarController.send(Snackbar(UiText.ResourceString(Res.string.not_enough_cards)))
                return@launch
            }

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
        _state.update { it.copy(carouselPage = cardIndex) }
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