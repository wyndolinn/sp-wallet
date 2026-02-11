package com.wynndie.spwallet.sharedFeature.home.presentation.screens.home

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wynndie.spwallet.sharedCore.domain.error.onError
import com.wynndie.spwallet.sharedCore.domain.error.onSuccess
import com.wynndie.spwallet.sharedCore.domain.repositories.CardsRepository
import com.wynndie.spwallet.sharedCore.domain.repositories.PreferencesRepository
import com.wynndie.spwallet.sharedCore.domain.repositories.UserRepository
import com.wynndie.spwallet.sharedCore.presentation.controllers.navigation.NavController
import com.wynndie.spwallet.sharedCore.presentation.controllers.overlay.OverlayController
import com.wynndie.spwallet.sharedCore.presentation.controllers.overlay.OverlayType
import com.wynndie.spwallet.sharedCore.presentation.extensions.asUiText
import com.wynndie.spwallet.sharedCore.presentation.formatters.displayableValue.OreDisplayableValue
import com.wynndie.spwallet.sharedCore.presentation.formatters.input.InputFilterOptions
import com.wynndie.spwallet.sharedCore.presentation.formatters.input.cutOffAt
import com.wynndie.spwallet.sharedCore.presentation.formatters.input.filterBy
import com.wynndie.spwallet.sharedCore.presentation.models.cards.AuthedCardUi
import com.wynndie.spwallet.sharedCore.presentation.models.cards.CustomCardUi
import com.wynndie.spwallet.sharedCore.presentation.models.cards.UnauthedCardUi
import com.wynndie.spwallet.sharedCore.presentation.states.LoadingState
import com.wynndie.spwallet.sharedFeature.home.domain.useCases.AuthCardUseCase
import com.wynndie.spwallet.sharedFeature.home.domain.useCases.DeleteAuthedCardUseCase
import com.wynndie.spwallet.sharedFeature.home.domain.useCases.SyncWithRemoteUseCase
import com.wynndie.spwallet.sharedFeature.home.domain.validators.TokenValidator
import com.wynndie.spwallet.sharedFeature.home.domain.validators.UuidValidator
import com.wynndie.spwallet.sharedFeature.home.presentation.screens.home.HomeNavEvent.OnClickCustomCard
import com.wynndie.spwallet.sharedFeature.home.presentation.screens.home.HomeNavEvent.OnClickTransferBetweenCards
import com.wynndie.spwallet.sharedFeature.home.presentation.screens.home.HomeNavEvent.OnClickTransferByCard
import com.wynndie.spwallet.sharedFeature.home.presentation.screens.home.models.HomeCardsData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
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
    private val tokenValidator: TokenValidator
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
                authedCards = authedCards.filter {
                    it.server == selectedSever
                }.map(AuthedCardUi::of),
                unauthedCards = unauthedCard.filter {
                    it.server == selectedSever
                }.map(UnauthedCardUi::of),
                customCards = customCards.filter {
                    it.server == selectedSever
                }.map(CustomCardUi::of)
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

        combine(
            _state.map { it.idInputFieldState.value.text }.distinctUntilChanged(),
            _state.map { it.tokenInputFieldState.value.text }.distinctUntilChanged()
        ) { id, token ->

            val validationResults = listOf(
                uuidValidator.validate(id),
                tokenValidator.validate(token)
            ).map { it.first }

            _state.update {
                it.copy(isAuthButtonEnabled = validationResults.all { isValid -> isValid })
            }
        }.launchIn(viewModelScope)
    }


    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.OnRefresh -> {
                closeAllDialogs()
                syncWithRemote()
            }


            is HomeAction.OnSwipeCarousel -> {
                _state.update { state ->
                    state.copy(carouselPage = action.index)
                }
            }

            is HomeAction.OnClickServerOption -> {
                viewModelScope.launch {
                    preferencesRepository.setSelectedSpServer(action.server)
                }
            }


            is HomeAction.OnToggleAuthCardSheet -> {
                _state.update { state ->
                    state.copy(
                        isAuthCardSheetVisible = action.isOpen,
                        isAuthedCardSheetVisible = false,
                        isDeactivateCardDialogVisible = false
                    )
                }
            }

            is HomeAction.OnToggleAuthedCardSheet -> {
                _state.update { state ->
                    state.copy(
                        isAuthCardSheetVisible = false,
                        isAuthedCardSheetVisible = action.isOpen,
                        isDeactivateCardDialogVisible = false
                    )
                }
            }

            is HomeAction.OnToggleDeleteCardDialog -> {
                _state.update { state ->
                    state.copy(
                        isAuthCardSheetVisible = false,
                        isAuthedCardSheetVisible = true,
                        isDeactivateCardDialogVisible = action.isOpen
                    )
                }
            }


            is HomeAction.OnClickTransferBetweenCard -> {


                viewModelScope.launch {
                    NavController.navigate(OnClickTransferBetweenCards(action.cardId))
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
                                        idInputFieldState = state.idInputFieldState.copy(
                                            value = TextFieldValue("")
                                        ),
                                        tokenInputFieldState = state.tokenInputFieldState.copy(
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
                    NavController.navigate(OnClickTransferByCard(action.cardId))
                    closeAllDialogs()
                }
            }


            is HomeAction.OnClickCustomCard -> {
                viewModelScope.launch {
                    NavController.navigate(OnClickCustomCard(action.cardId))
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
                    .filterBy(InputFilterOptions.Uuid.predicate)
                    .cutOffAt(state.value.idInputFieldState.maxLength) ?: return

                _state.update { state ->
                    state.copy(
                        idInputFieldState = state.idInputFieldState.copy(
                            value = value
                        )
                    )
                }
            }

            is HomeAction.OnChangeCardTokenValue -> {
                val value = action.value
                    .filterBy(InputFilterOptions.Base64.predicate)
                    .cutOffAt(state.value.tokenInputFieldState.maxLength) ?: return

                _state.update { state ->
                    state.copy(
                        tokenInputFieldState = state.tokenInputFieldState.copy(
                            value = value
                        )
                    )
                }
            }

            HomeAction.OnToggleCardIdFocus -> {
                isCardIdValid(state.value.idInputFieldState.value.text)
            }

            HomeAction.OnToggleCardTokenFocus -> {
                isCardTokenValid(state.value.tokenInputFieldState.value.text)
            }
        }
    }

    private fun syncWithRemote() {
        viewModelScope.launch {
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
    }

    private fun updateBalance() {
        val authedCardsBalance = state.value.authedCards.sumOf { it.balance.value }
        val customCardsBalance = state.value.customCards.sumOf { it.balance.value }
        val totalBalance = authedCardsBalance + customCardsBalance
        _state.update {
            it.copy(totalBalance = OreDisplayableValue.of(totalBalance))
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
                tokenInputFieldState = state.tokenInputFieldState.copy(
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
                idInputFieldState = state.idInputFieldState.copy(
                    supportingText = error?.asUiText(),
                    hasError = !isValid
                )
            )
        }
        return isValid
    }
}