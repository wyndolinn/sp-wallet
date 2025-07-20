package com.wynndie.spwallet.sharedFeature.transfer.presentation.screen.searchRecipient

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wynndie.spwallet.sharedCore.domain.repository.RecipientRepository
import com.wynndie.spwallet.sharedCore.presentation.model.UiRecipient
import com.wynndie.spwallet.sharedCore.presentation.model.input.InputFilterOptions
import com.wynndie.spwallet.sharedCore.presentation.model.input.cutOffAt
import com.wynndie.spwallet.sharedCore.presentation.model.input.filterBy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class SearchRecipientViewModel(
    private val args: SearchRecipientViewModelArgs,
    private val recipientRepository: RecipientRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SearchRecipientState())
    val state = _state.asStateFlow()

    init {
        recipientRepository.getRecipients().onEach { recipients ->
            _state.update { state ->
                state.copy(recipients = recipients.map { UiRecipient.of(it) })
            }
        }.launchIn(viewModelScope)
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
                args.onClickEditRecipient(action.recipientId)
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