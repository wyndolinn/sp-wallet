package com.wynndie.spwallet.sharedFeature.profile.presentation.screen.localization

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ThemeViewModel(
    private val args: ThemeViewModelArgs
) : ViewModel() {

    private val _state = MutableStateFlow(ThemeState())
    val state = _state.asStateFlow()


    fun onAction(action: ThemeAction) {
        when (action) {
            ThemeAction.OnClickBack -> {
                args.onClickBack()
            }

            is ThemeAction.OnClickLanguage -> {
                args.onClickBack()
            }
        }
    }
}