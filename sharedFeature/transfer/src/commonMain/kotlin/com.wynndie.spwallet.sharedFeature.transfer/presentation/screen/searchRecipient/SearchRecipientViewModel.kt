@file:OptIn(FlowPreview::class)

package com.wynndie.spwallet.sharedFeature.transfer.presentation.screen.searchRecipient

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wynndie.spwallet.sharedCore.domain.constants.Constants
import com.wynndie.spwallet.sharedCore.domain.repository.RecipientRepository
import com.wynndie.spwallet.sharedCore.presentation.model.UiRecipient
import com.wynndie.spwallet.sharedCore.presentation.model.input.InputFilterOptions
import com.wynndie.spwallet.sharedCore.presentation.model.input.cutOffAt
import com.wynndie.spwallet.sharedCore.presentation.model.input.filterBy
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class SearchRecipientViewModel(
    private val args: SearchRecipientViewModelArgs,
    private val recipientRepository: RecipientRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SearchRecipientState())
    val state = _state.asStateFlow()

    private var cachedRecipients = emptyList<UiRecipient>()

    init {
        recipientRepository.getRecipients().onEach { recipients ->
            cachedRecipients = recipients.map { UiRecipient.of(it) }
        }.launchIn(viewModelScope)

        _state
            .map { it.recipientInputFieldState.value.text }
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
                                    recipient.number.contains(query) || recipient.name.contains(query)
                                }
                            )
                        }
                    }
                }

                val isCardNumberEntered =
                    query.length == Constants.CARD_NUMBER_LENGTH && query.all { it.isDigit() }
                _state.update { state ->
                    state.copy(isNewRecipient = isCardNumberEntered && state.recipients.isEmpty())
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: SearchRecipientAction) {
        when (action) {
            SearchRecipientAction.OnClickBack -> {
                args.onClickBack()
            }

            is SearchRecipientAction.OnClickRecipient -> {
                args.onClickRecipient(action.id, action.cardNumber)
            }

            is SearchRecipientAction.OnClickEditRecipient -> {
                args.onClickEditRecipient(action.id)
            }

            is SearchRecipientAction.OnChangeRecipientValue -> {
                val value = action.value
                    .filterBy(InputFilterOptions.Text.LettersOrDigits.predicate)
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
    }
}