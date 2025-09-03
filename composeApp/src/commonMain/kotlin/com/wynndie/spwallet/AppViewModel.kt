package com.wynndie.spwallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wynndie.spwallet.sharedCore.domain.repository.DataStoreRepository
import com.wynndie.spwallet.sharedFeature.profile.domain.model.LocalizationController
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class AppViewModel(
    dataStoreRepository: DataStoreRepository,
    private val localizationController: LocalizationController
) : ViewModel() {

    init {
        dataStoreRepository.getLocalization().onEach { languageIso ->
            localizationController.applyLanguage(languageIso)
        }.launchIn(viewModelScope)
    }
}