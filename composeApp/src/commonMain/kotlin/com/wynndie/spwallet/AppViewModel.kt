package com.wynndie.spwallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wynndie.spwallet.sharedCore.presentation.controller.overlay.OverlayType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppViewModel : ViewModel() {

    private val _dialogEvent = MutableStateFlow<OverlayType?>(null)
    val dialogEvent = _dialogEvent.asStateFlow()

    fun sendDialogEvent(dialog: OverlayType?) {
        viewModelScope.launch {
            _dialogEvent.value = dialog
        }
    }
}