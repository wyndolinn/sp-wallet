package com.wynndie.spwallet.sharedFeature.transfer.presentation.searchRecipient

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wynndie.spwallet.sharedCore.domain.models.cards.RecipientCard
import com.wynndie.spwallet.sharedCore.domain.repositories.PreferencesRepository
import com.wynndie.spwallet.sharedCore.domain.repositories.RecipientRepository
import com.wynndie.spwallet.sharedCore.presentation.controllers.navigation.NavEventController
import com.wynndie.spwallet.sharedCore.presentation.formatters.InputFilters
import com.wynndie.spwallet.sharedCore.presentation.extensions.cutOffAt
import com.wynndie.spwallet.sharedCore.presentation.extensions.filter
import com.wynndie.spwallet.sharedCore.presentation.extensions.trimSpaces
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchRecipientViewModel(
    recipientRepository: RecipientRepository,
    preferencesRepository: PreferencesRepository,
    private val navEventController: NavEventController
) : ViewModel() {

    private val _state = MutableStateFlow(SearchRecipientState())
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

        _state
            .map { it.recipientInputFieldState.value.text }
            .distinctUntilChanged()
            .onEach { query ->
                _state.update { state ->
                    val recipients = if (query.isNotBlank()) {
                        cachedRecipients.filter {
                            it.number.contains(query) || it.name.contains(query)
                        }
                    } else cachedRecipients

                    val cardEntered = query.length == 5 && query.all { it.isDigit() }
                    state.copy(
                        recipients = recipients,
                        isNewRecipient = cardEntered && recipients.isEmpty()
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: SearchRecipientAction) {
        when (action) {
            SearchRecipientAction.NavigateBack -> navigateBack()
            is SearchRecipientAction.SelectRecipient -> selectRecipient(action.cardNumber)
            is SearchRecipientAction.ChangeRecipientValue -> changeRecipientValue(action.value)
        }
    }


    private fun navigateBack() {
        viewModelScope.launch {
            navEventController.navigate(SearchRecipientNavEvent.NavigateBack)
        }
    }

    private fun selectRecipient(card: String) {
        viewModelScope.launch {
            navEventController.navigate(
                SearchRecipientNavEvent.NavigateToTransfer(card)
            )
        }
    }

    private fun changeRecipientValue(value: TextFieldValue) {
        val value = value
            .filter(InputFilters.PlainText.predicate)
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
}