package com.wynndie.spwallet.sharedFeature.transfer.presentation.screen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RecipientViewModel : ViewModel() {

    private val _recipientId = MutableStateFlow<String?>(null)
    val recipientId = _recipientId.asStateFlow()

    private val _recipientCardNumber = MutableStateFlow("")
    val recipientCardNumber = _recipientCardNumber.asStateFlow()

    fun setRecipientId(value: String?) {
        _recipientId.value = value
    }

    fun setRecipientCardNumber(value: String) {
        _recipientCardNumber.value = value
    }
}