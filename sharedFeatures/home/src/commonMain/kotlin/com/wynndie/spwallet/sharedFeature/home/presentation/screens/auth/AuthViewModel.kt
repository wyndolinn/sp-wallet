package com.wynndie.spwallet.sharedFeature.home.presentation.screens.auth

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wynndie.spwallet.sharedCore.domain.models.SpServers
import com.wynndie.spwallet.sharedCore.domain.outcome.getOrElse
import com.wynndie.spwallet.sharedCore.domain.repositories.PreferencesRepository
import com.wynndie.spwallet.sharedCore.presentation.controllers.navigation.NavEventController
import com.wynndie.spwallet.sharedCore.presentation.controllers.overlay.Snackbar
import com.wynndie.spwallet.sharedCore.presentation.controllers.overlay.SnackbarController
import com.wynndie.spwallet.sharedCore.presentation.extensions.asUiText
import com.wynndie.spwallet.sharedCore.presentation.extensions.cutOffAt
import com.wynndie.spwallet.sharedCore.presentation.extensions.filter
import com.wynndie.spwallet.sharedCore.presentation.extensions.observeInputField
import com.wynndie.spwallet.sharedCore.presentation.extensions.observeValidationStates
import com.wynndie.spwallet.sharedCore.presentation.extensions.validateInputField
import com.wynndie.spwallet.sharedCore.presentation.formatters.InputFilters
import com.wynndie.spwallet.sharedCore.presentation.formatters.LoadingState
import com.wynndie.spwallet.sharedFeature.home.domain.useCases.AuthCardUseCase
import com.wynndie.spwallet.sharedFeature.home.domain.useCases.SyncWithRemoteUseCase
import com.wynndie.spwallet.sharedFeature.home.domain.validators.TokenValidator
import com.wynndie.spwallet.sharedFeature.home.domain.validators.UuidValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    preferencesRepository: PreferencesRepository,
    private val authCardUseCase: AuthCardUseCase,
    private val syncWithRemoteUseCase: SyncWithRemoteUseCase,
    private val uuidValidator: UuidValidator,
    private val tokenValidator: TokenValidator,
    private val navEventController: NavEventController,
    private val snackbarController: SnackbarController
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state = _state.asStateFlow()

    private lateinit var selectedServer: SpServers


    init {
        preferencesRepository.getSelectedSpServer().onEach { server ->
            selectedServer = server
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


    fun onAction(action: AuthAction) {
        when (action) {
            AuthAction.NavigateBack -> navigateBack()
            is AuthAction.AuthCard -> authCard(action.id, action.token)
            is AuthAction.ChangeIdValue -> changeIdValue(action.value)
            is AuthAction.ChangeTokenValue -> changeTokenValue(action.value)
            AuthAction.ClearTokenFocus -> clearTokenFocus()
            AuthAction.ClearIdFocus -> clearIdFocus()
            is AuthAction.ToggleHelpSheet -> toggleHelpSheet(action.isOpen)
        }
    }


    private fun authCard(id: String, token: String) {
        viewModelScope.launch {
            _state.update { it.copy(loadingState = LoadingState.Loading) }

            authCardUseCase(
                server = selectedServer,
                id = id,
                token = token
            ).getOrElse { error ->
                snackbarController.send(Snackbar(error.asUiText()))
                _state.update { it.copy(loadingState = LoadingState.Finished) }
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
                    )
                )
            }

            _state.update { it.copy(loadingState = LoadingState.Finished) }
        }
    }

    private fun changeIdValue(value: TextFieldValue) {
        val value = value
            .filter(InputFilters.Uuid.predicate)
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
            .filter(InputFilters.Base64.predicate)
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

    private fun toggleHelpSheet(isOpen: Boolean) {
        _state.update { it.copy(isHelpSheetOpen = isOpen) }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            navEventController.navigate(AuthNavEvent.NavigateBack)
        }
    }
}