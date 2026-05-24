package com.wynndie.spwallet.sharedFeature.edit.presentation.screens.recipients

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wynndie.spwallet.sharedCore.domain.constants.emptyRecipientCard
import com.wynndie.spwallet.sharedCore.domain.models.cards.RecipientCard
import com.wynndie.spwallet.sharedCore.domain.repositories.PreferencesRepository
import com.wynndie.spwallet.sharedCore.domain.repositories.RecipientRepository
import com.wynndie.spwallet.sharedCore.presentation.controllers.navigation.NavEventController
import com.wynndie.spwallet.sharedCore.presentation.controllers.overlay.Snackbar
import com.wynndie.spwallet.sharedCore.presentation.controllers.overlay.SnackbarController
import com.wynndie.spwallet.sharedCore.presentation.extensions.cutOffAt
import com.wynndie.spwallet.sharedCore.presentation.extensions.filter
import com.wynndie.spwallet.sharedCore.presentation.extensions.observeInputField
import com.wynndie.spwallet.sharedCore.presentation.extensions.trimSpaces
import com.wynndie.spwallet.sharedCore.presentation.formatters.InputFilters
import com.wynndie.spwallet.sharedCore.presentation.formatters.LoadingState
import com.wynndie.spwallet.sharedCore.presentation.formatters.UiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecipientsViewModel(
    private val preferencesRepository: PreferencesRepository,
    private val recipientRepository: RecipientRepository,
    private val navEventController: NavEventController,
    private val snackbarController: SnackbarController
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
        }
    }

    private fun deleteRecipient() {
        viewModelScope.launch {
            val recipient = _state.value.selectedRecipient ?: return@launch
            recipientRepository.deleteRecipient(recipient)
            snackbarController.send(Snackbar(UiText.DynamicString("Получатель удалён")))
        }
    }

    private fun clearCardNumberFocus() {

    }

    private fun clearCardNameFocus() {

    }

    private fun navigateBack() {
        viewModelScope.launch {
            navEventController.navigate(RecipientsNavEvent.NavigateBack)
        }
    }

    private fun selectRecipient(recipient: RecipientCard?) {
        _state.update { it.copy(selectedRecipient = recipient) }
    }

    private fun toggleDeleteRecipientDialog(open: Boolean) {
        _state.update { it.copy(isDeleteDialogOpen = open) }
    }

    private fun toggleEditRecipientSheet(open: Boolean) {
        _state.update { it.copy(isEditRecipientSheetOpen = open) }
    }
}