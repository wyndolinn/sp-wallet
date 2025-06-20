package com.wynndie.spwallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wynndie.spwallet.sharedCore.presentation.controller.dialog.Dialog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppViewModel : ViewModel() {

    private val _dialogEvent = MutableStateFlow<Dialog?>(null)
    val dialogEvent = _dialogEvent.asStateFlow()

    fun sendDialogEvent(dialog: Dialog?) {
        viewModelScope.launch {
            _dialogEvent.value = dialog
        }
    }
}