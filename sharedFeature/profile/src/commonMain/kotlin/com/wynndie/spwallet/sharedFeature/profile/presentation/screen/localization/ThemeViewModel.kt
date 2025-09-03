package com.wynndie.spwallet.sharedFeature.profile.presentation.screen.localization

import androidx.lifecycle.ViewModel
import com.wynndie.spwallet.sharedFeature.profile.domain.model.Localization
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ThemeViewModel(
    private val args: ThemeViewModelArgs,
    private val localization: Localization
) : ViewModel() {

    private val _state = MutableStateFlow(ThemeState())
    val state = _state.asStateFlow()


    fun onAction(action: ThemeAction) {
        when (action) {
            ThemeAction.OnClickBack -> {
                args.onClickBack()
            }

            is ThemeAction.OnClickLanguage -> {
                localization.applyLanguage(action.languageIso)
                _state.update {
                    it.copy(selectedLanguageIso = action.languageIso)
                }
//                args.onClickBack()
            }
        }
    }
}