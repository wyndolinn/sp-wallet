package com.wynndie.spwallet.sharedFeature.edit.presentation.screens.recipients

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wynndie.spwallet.sharedCore.Res
import com.wynndie.spwallet.sharedCore.domain.constants.emptyRecipientCard
import com.wynndie.spwallet.sharedCore.domain.models.cards.RecipientCard
import com.wynndie.spwallet.sharedCore.domain.repositories.PreferencesRepository
import com.wynndie.spwallet.sharedCore.domain.repositories.RecipientRepository
import com.wynndie.spwallet.sharedCore.domain.validators.CardNameValidator
import com.wynndie.spwallet.sharedCore.domain.validators.CardNumberValidator
import com.wynndie.spwallet.sharedCore.presentation.controllers.navigation.NavEventController
import com.wynndie.spwallet.sharedCore.presentation.controllers.overlay.SnackbarController
import com.wynndie.spwallet.sharedCore.presentation.extensions.cutOffAt
import com.wynndie.spwallet.sharedCore.presentation.extensions.filter
import com.wynndie.spwallet.sharedCore.presentation.extensions.observeInputField
import com.wynndie.spwallet.sharedCore.presentation.extensions.observeValidationStates
import com.wynndie.spwallet.sharedCore.presentation.extensions.trimSpaces
import com.wynndie.spwallet.sharedCore.presentation.extensions.validateInputField
import com.wynndie.spwallet.sharedCore.presentation.formatters.InputFilters
import com.wynndie.spwallet.sharedCore.presentation.formatters.UiText
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
    private val cardNumberValidator: CardNumberValidator
) : ViewModel() {

    private val _state = MutableStateFlow(RecipientsState())
    val state = _state.asStateFlow()

    private var cachedRecipients = emptyList<RecipientCard>()


    init {
        combine(
            recipientRepository.getRecipients(),
            preferencesRepository.getSelectedSpServer()
        ) { recipients, server ->
            cachedRecipients = recipients.filter { it.server == server }
            _state.update { it.copy(recipients = cachedRecipients) }
        }.launchIn(viewModelScope)

        _state.observeInputField(
            inputField = { it.recipientInputFieldState }
        ) { inputFieldState ->
            val query = inputFieldState.value.text
            _state.update { state ->
                val recipients = if (query.isNotBlank()) {
                    cachedRecipients.filter {
                        it.number.contains(query) || it.name.contains(query)
                    }
                } else cachedRecipients

                state.copy(recipients = recipients)
            }
        }.launchIn(viewModelScope)

        observeValidationStates(
            _state.observeInputField(
                inputField = { it.cardNameInputFieldState },
                validation = { cardNameValidator.validate(it) },
                updateState = { _state.update { state -> state.copy(cardNameInputFieldState = it) } }
            ),
            _state.observeInputField(
                inputField = { it.cardNumberInputFieldState },
                validation = { cardNumberValidator.validate(it) },
                updateState = { _state.update { state -> state.copy(cardNumberInputFieldState = it) } }
            )
        ).onEach { isAllValid ->
            _state.update { it.copy(isSaveButtonEnabled = isAllValid) }
        }.launchIn(viewModelScope)
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
            RecipientsAction.DeleteRecipient -> deleteRecipient()
            RecipientsAction.SaveRecipient -> saveRecipient()
            RecipientsAction.MakeTransfer -> makeTransfer()
            is RecipientsAction.ToggleEditRecipientSheet -> toggleEditRecipientSheet(action.isOpen)
            is RecipientsAction.ToggleDeleteRecipientDialog -> toggleDeleteRecipientDialog(action.isOpen)
        }
    }


    private fun changeRecipientValue(value: TextFieldValue) {
        val value = value
            .filter(InputFilters.Text.predicate)
            .trimSpaces()
            .cutOffAt(state.value.recipientInputFieldState.maxLength) ?: return

        _state.update { state ->
            state.copy(
                recipientInputFieldState = state.recipientInputFieldState.copy(
                    value = value
                )
            )
        }
    }

    private fun changeCardNameValue(value: TextFieldValue) {
        val value = value
            .filter(InputFilters.Text.predicate)
            .trimSpaces()
            .cutOffAt(state.value.cardNameInputFieldState.maxLength) ?: return

        _state.update { state ->
            state.copy(
                cardNameInputFieldState = state.cardNameInputFieldState.copy(
                    value = value
                )
            )
        }
    }

    private fun changeCardNumberValue(value: TextFieldValue) {
        val value = value
            .filter(InputFilters.Numbers.predicate)
            .trimSpaces()
            .cutOffAt(state.value.cardNumberInputFieldState.maxLength) ?: return

        _state.update { state ->
            state.copy(
                cardNumberInputFieldState = state.cardNumberInputFieldState.copy(
                    value = value
                )
            )
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
                name = _state.value.cardNameInputFieldState.value.text,
                number = _state.value.cardNumberInputFieldState.value.text,
                server = preferencesRepository.getSelectedSpServer().first()
            )
            recipientRepository.insertRecipient(modifiedRecipient)
            closeOverlays()
        }
    }

    private fun deleteRecipient() {
        viewModelScope.launch {
            val recipient = _state.value.selectedRecipient ?: return@launch
            recipientRepository.deleteRecipient(recipient)
            snackbarController.send(UiText.ResourceString(Res.string.recipient_deleted))
            closeOverlays()
        }
    }

    private fun clearCardNumberFocus() {
        _state.validateInputField(
            inputField = { it.cardNumberInputFieldState },
            validation = { cardNumberValidator.validate(it) },
            updateState = { _state.update { state -> state.copy(cardNumberInputFieldState = it) } }
        )
    }

    private fun clearCardNameFocus() {
        _state.validateInputField(
            inputField = { it.cardNameInputFieldState },
            validation = { cardNameValidator.validate(it) },
            updateState = { _state.update { state -> state.copy(cardNameInputFieldState = it) } }
        )
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
                cardNameInputFieldState = state.cardNameInputFieldState.copy(
                    value = TextFieldValue(name, TextRange(name.length))
                ),
                cardNumberInputFieldState = state.cardNumberInputFieldState.copy(
                    value = TextFieldValue(number, TextRange(number.length))
                )
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
                isDeleteDialogOpen = false
            )
        }
    }
}