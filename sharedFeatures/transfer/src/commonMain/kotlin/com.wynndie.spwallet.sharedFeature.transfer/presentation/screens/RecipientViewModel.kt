package com.wynndie.spwallet.sharedFeature.transfer.presentation.screens

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RecipientViewModel : ViewModel() {

    private val _recipientCardNumber = MutableStateFlow("")
    val recipientCardNumber = _recipientCardNumber.asStateFlow()

    fun setRecipientCardNumber(value: String) {
        _recipientCardNumber.value = value
    }
}