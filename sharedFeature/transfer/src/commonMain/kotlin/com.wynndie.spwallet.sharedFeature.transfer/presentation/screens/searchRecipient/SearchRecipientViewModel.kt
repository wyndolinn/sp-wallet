package com.wynndie.spwallet.sharedFeature.transfer.presentation.screens.searchRecipient

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wynndie.spwallet.sharedCore.domain.constants.CoreConstants
import com.wynndie.spwallet.sharedCore.domain.repositories.RecipientRepository
import com.wynndie.spwallet.sharedCore.presentation.controllers.navigation.NavController
import com.wynndie.spwallet.sharedCore.presentation.extensions.cutOffAt
import com.wynndie.spwallet.sharedCore.presentation.extensions.filterBy
import com.wynndie.spwallet.sharedCore.presentation.formatters.InputFilterOptions
import com.wynndie.spwallet.sharedCore.presentation.models.cards.RecipientCardUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchRecipientViewModel(
    recipientRepository: RecipientRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SearchRecipientState())
    val state = _state.asStateFlow()

    private var cachedRecipients = emptyList<RecipientCardUi>()

    init {
        recipientRepository.getRecipients().onEach { recipients ->
            cachedRecipients = recipients.map { RecipientCardUi.of(it) }
        }.launchIn(viewModelScope)

        _state
            .map { it.recipientInputField.value.text }
            .distinctUntilChanged()
            .onEach { query ->
                when {
                    query.isBlank() -> {
                        _state.update {
                            it.copy(
                                recipients = cachedRecipients
                            )
                        }
                    }

                    else -> {
                        _state.update { state ->
                            state.copy(
                                recipients = cachedRecipients.filter { recipient ->
                                    recipient.cardNumber.contains(query) || recipient.name.contains(
                                        query
                                    )
                                }
                            )
                        }
                    }
                }

                val isCardNumberEntered =
                    query.length == CoreConstants.CARD_NUMBER_LENGTH && query.all { it.isDigit() }
                _state.update { state ->
                    state.copy(isNewRecipient = isCardNumberEntered && state.recipients.isEmpty())
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: SearchRecipientAction) {
        when (action) {
            SearchRecipientAction.OnClickBack -> {
                viewModelScope.launch {
                    NavController.navigate(SearchRecipientNavEvent.OnClickBack)
                }
            }

            is SearchRecipientAction.OnClickRecipient -> {
                viewModelScope.launch {
                    NavController.navigate(
                        SearchRecipientNavEvent.OnClickRecipient(
                            id = action.id,
                            cardNumber = action.cardNumber
                        )
                    )
                }
            }

            is SearchRecipientAction.OnClickEditRecipient -> {
                viewModelScope.launch {
                    NavController.navigate(
                        SearchRecipientNavEvent.OnClickEditRecipient(id = action.id)
                    )
                }
            }

            is SearchRecipientAction.OnChangeRecipientValue -> {
                val value = action.value
                    .filterBy(InputFilterOptions.Text.LettersOrDigits.predicate)
                    .cutOffAt(state.value.recipientInputField.maxLength) ?: return

                _state.update { state ->
                    state.copy(
                        recipientInputField = state.recipientInputField.copy(
                            value = value
                        )
                    )
                }
            }
        }
    }
}