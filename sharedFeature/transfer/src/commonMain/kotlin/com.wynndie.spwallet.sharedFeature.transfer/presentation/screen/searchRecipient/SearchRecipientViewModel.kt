package com.wynndie.spwallet.sharedFeature.transfer.presentation.screen.searchRecipient

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SearchRecipientViewModel(
    private val args: SearchRecipientViewModelArgs,

) : ViewModel() {

    private val _state = MutableStateFlow(SearchRecipientState())
    val state = _state.asStateFlow()

    init {

    }

    fun onAction(action: SearchRecipientAction) {
        when (action) {
            is SearchRecipientAction.OnClickRecipient -> {
                args.onClickRecipient(action.id, action.cardNumber)
            }

            is SearchRecipientAction.OnClickEditRecipient -> {
                args.onClickEditRecipient(action.recipientId)
            }

            is SearchRecipientAction.OnChangeRecipientValue -> {

            }
        }
    }
}