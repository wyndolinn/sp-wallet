package com.wynndie.spwallet.sharedFeature.edit.presentation.screens.recipients

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wynndie.spwallet.sharedCore.Res
import com.wynndie.spwallet.sharedCore.domain.helpers.emptyRecipientCard
import com.wynndie.spwallet.sharedCore.domain.models.cards.RecipientCard
import com.wynndie.spwallet.sharedCore.domain.repositories.PreferencesRepository
import com.wynndie.spwallet.sharedCore.domain.repositories.RecipientRepository
import com.wynndie.spwallet.sharedCore.domain.validators.CardNameValidator
import com.wynndie.spwallet.sharedCore.domain.validators.CardNumberValidator
import com.wynndie.spwallet.sharedCore.presentation.controllers.navigation.NavEventController
import com.wynndie.spwallet.sharedCore.presentation.controllers.overlay.SnackbarController
import com.wynndie.spwallet.sharedCore.presentation.extensions.cutOffAt
import com.wynndie.spwallet.sharedCore.presentation.extensions.filter
import com.wynndie.spwallet.sharedCore.presentation.extensions.observeValidStates
import com.wynndie.spwallet.sharedCore.presentation.extensions.resetValidationState
import com.wynndie.spwallet.sharedCore.presentation.extensions.select
import com.wynndie.spwallet.sharedCore.presentation.extensions.trimSpaces
import com.wynndie.spwallet.sharedCore.presentation.extensions.validateInputField
import com.wynndie.spwallet.sharedCore.presentation.models.InputFilters
import com.wynndie.spwallet.sharedCore.presentation.models.UiText
import com.wynndie.spwallet.sharedCore.recipient_deleted
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecipientsViewModel(
    private val preferencesRepository: PreferencesRepository,
    private val recipientRepository: RecipientRepository,
    private val navEventController: NavEventController,
    private val snackbarController: SnackbarController,
    private val cardNameValidator: CardNameValidator,
    private val cardNumberValidator: CardNumberValidator,
) : ViewModel() {

    private val _state = MutableStateFlow(RecipientsState())
    val state = _state.asStateFlow()

    private var cachedRecipients = emptyList<RecipientCard>()


    init {
        observerRecipients()
        observeSaveRecipientValidationStates()
    }


    fun onAction(action: RecipientsAction) {
        when (action) {
            RecipientsAction.NavigateBack -> navigateBack()
            is RecipientsAction.SelectRecipient -> selectRecipient(action.recipient)
            is RecipientsAction.ChangeRecipientValue -> changeRecipientValue(action.value)
            is RecipientsAction.ChangeCardNameValue -> changeCardNameValue(action.value)
            is RecipientsAction.ChangeCardNumberValue -> changeCardNumberValue(action.value)
            RecipientsAction.ClearCardNameFocus -> clearCardNameFocus()
            RecipientsAction.ClearCardNumberFocus -> clearCardNumberFocus()
            is RecipientsAction.DeleteRecipient -> deleteRecipient(action.recipient)
            RecipientsAction.SaveRecipient -> saveRecipient()
            RecipientsAction.MakeTransfer -> makeTransfer()
            is RecipientsAction.ToggleEditRecipientSheet -> toggleEditRecipientSheet(action.isOpen)
            is RecipientsAction.ToggleDeleteRecipientDialog -> toggleDeleteRecipientDialog(action.isOpen)
        }
    }


    private fun changeRecipientValue(value: TextFieldValue) {
        _state.update { state ->
            val filteredValue = value
                .filter(InputFilters.Text)
                .trimSpaces()
                .cutOffAt(state.recipientInputFieldState.maxLength) ?: return@update state

            state.copy(
                recipientInputFieldState = state.recipientInputFieldState.copy(
                    value = filteredValue,
                ),
            )
        }
    }

    private fun changeCardNameValue(value: TextFieldValue) {
        _state.update { state ->
            val filteredValue = value
                .filter(InputFilters.Text)
                .trimSpaces()
                .cutOffAt(state.nameInputFieldState.maxLength) ?: return@update state

            state.copy(nameInputFieldState = state.nameInputFieldState.copy(value = filteredValue))
        }
    }

    private fun changeCardNumberValue(value: TextFieldValue) {
        _state.update { state ->
            val filteredValue = value
                .filter(InputFilters.Numbers)
                .trimSpaces()
                .cutOffAt(state.numberInputFieldState.maxLength) ?: return@update state

            state.copy(numberInputFieldState = state.numberInputFieldState.copy(value = filteredValue))
        }
    }

    private fun makeTransfer() {
        viewModelScope.launch {
            val recipient = _state.value.selectedRecipient ?: return@launch
            navEventController.navigate(RecipientsNavEvent.NavigateToTransfer(recipient.number))
            closeOverlays()
        }
    }

    private fun saveRecipient() {
        viewModelScope.launch {
            val recipient = _state.value.selectedRecipient ?: emptyRecipientCard
            val modifiedRecipient = recipient.copy(
                name = _state.value.nameInputFieldState.value.text,
                number = _state.value.numberInputFieldState.value.text,
                server = preferencesRepository.getSelectedServer().first(),
            )
            recipientRepository.insertRecipient(modifiedRecipient)
            closeOverlays()
        }
    }

    private fun deleteRecipient(recipient: RecipientCard) {
        viewModelScope.launch {
            recipientRepository.deleteRecipient(recipient)
            snackbarController.send(UiText.ResourceString(Res.string.recipient_deleted))
            closeOverlays()
        }
    }

    private fun clearCardNumberFocus() {
        _state.validateInputField(
            inputField = { it.numberInputFieldState },
            validator = cardNumberValidator::validate
        )
        { copy(numberInputFieldState = it) }
    }

    private fun clearCardNameFocus() {
        _state.validateInputField(
            inputField = { it.numberInputFieldState },
            validator = cardNameValidator::validate
        )
        { copy(nameInputFieldState = it) }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            navEventController.navigate(RecipientsNavEvent.NavigateBack)
        }
    }

    private fun selectRecipient(recipient: RecipientCard?) {
        _state.update { state ->
            val name = recipient?.name ?: ""
            val number = recipient?.number ?: ""
            state.copy(
                selectedRecipient = recipient,
                nameInputFieldState = state.nameInputFieldState.copy(
                    value = TextFieldValue(name, TextRange(name.length)),
                ),
                numberInputFieldState = state.numberInputFieldState.copy(
                    value = TextFieldValue(number, TextRange(number.length)),
                ),
            )
        }
    }

    private fun toggleDeleteRecipientDialog(open: Boolean) {
        _state.update { it.copy(isDeleteDialogOpen = open) }
    }

    private fun toggleEditRecipientSheet(open: Boolean) {
        _state.update { it.copy(isEditRecipientSheetOpen = open) }
    }

    private fun closeOverlays() {
        _state.update {
            it.copy(
                selectedRecipient = null,
                isEditRecipientSheetOpen = false,
                isDeleteDialogOpen = false,
            )
        }
    }

    private fun observeSaveRecipientValidationStates() {
        _state
            .select { it.nameInputFieldState.value }
            .onEach {
                _state.resetValidationState(
                    inputField = { it.nameInputFieldState },
                    validator = cardNameValidator::validate,
                ) { copy(nameInputFieldState = it) }
            }.launchIn(viewModelScope)

        _state
            .select { it.numberInputFieldState.value }
            .onEach {
                _state.resetValidationState(
                    inputField = { it.numberInputFieldState },
                    validator = cardNumberValidator::validate,
                ) { copy(numberInputFieldState = it) }
            }.launchIn(viewModelScope)

        _state.observeValidStates(
            _state.select { it.nameInputFieldState.hasError },
            _state.select { it.numberInputFieldState.hasError },
        ) { copy(isSaveButtonEnabled = it) }.launchIn(viewModelScope)
    }

    private fun observerRecipients() {
        combine(
            recipientRepository.getRecipients(),
            preferencesRepository.getSelectedServer(),
        ) { recipients, server ->
            cachedRecipients = recipients.filter { it.server == server }
            _state.update { it.copy(recipients = cachedRecipients) }
        }.launchIn(viewModelScope)

        _state
            .select { it.recipientInputFieldState }
            .onEach { recipient ->
                val query = recipient.value.text
                _state.update { state ->
                    val recipients = if (query.isNotBlank()) {
                        cachedRecipients.filter {
                            it.number.contains(query) || it.name.contains(query)
                        }
                    } else cachedRecipients

                    state.copy(recipients = recipients)
                }
            }.launchIn(viewModelScope)
    }
}