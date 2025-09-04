package com.wynndie.spwallet.sharedFeature.profile.presentation.screen.localization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wynndie.spwallet.sharedCore.domain.repository.DataStoreRepository
import com.wynndie.spwallet.sharedCore.domain.controller.localization.LocalizationController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.option.viewModelScopeFactory

class ThemeViewModel(
    private val dataStoreRepository: DataStoreRepository,
    private val args: ThemeViewModelArgs,
    private val localizationController: LocalizationController
) : ViewModel() {

    private val _state = MutableStateFlow(ThemeState())
    val state = _state.asStateFlow()


    init {
        dataStoreRepository.getLocalization().onEach { languageIso ->
            localizationController.applyLanguage(languageIso)
            _state.update {
                it.copy(selectedLanguageIso = localizationController.getLanguageIso())
            }
        }.launchIn(viewModelScope)
    }


    fun onAction(action: ThemeAction) {
        when (action) {
            ThemeAction.OnClickBack -> {
                args.onClickBack()
            }

            is ThemeAction.OnClickLanguage -> {
                viewModelScope.launch {
                    dataStoreRepository.setLocalization(action.languageIso)
                }
            }
        }
    }
}