package com.wynndie.spwallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wynndie.spwallet.sharedCore.domain.repository.DataStoreRepository
import com.wynndie.spwallet.sharedCore.domain.controller.localization.LocalizationController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppViewModel(
    private val dataStoreRepository: DataStoreRepository,
    private val localizationController: LocalizationController
) : ViewModel() {

    init {
//        dataStoreRepository.getLocalization().onEach { languageIso ->
//            println("PRINTLINE: receivedLocale $languageIso")
//            localizationController.applyLanguage(languageIso)
//        }.launchIn(viewModelScope)
    }

    fun func() {
//        viewModelScope.launch {
//            val currentLocale = localizationController.getLanguageIso()
//            val savedLocale = dataStoreRepository.getLocalization().first()
//            println("PRINTLINE: currentLocale $currentLocale")
//            println("PRINTLINE: savedLocale $savedLocale")
//            if (savedLocale != currentLocale) {
//                localizationController.applyLanguage(savedLocale)
//            }
//        }
    }
}