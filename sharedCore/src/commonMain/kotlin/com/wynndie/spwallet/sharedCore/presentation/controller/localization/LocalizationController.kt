package com.wynndie.spwallet.sharedCore.presentation.controller.localization

interface LocalizationController {

    fun applyLanguage(iso: String)
    fun getLanguageIso(): String
}