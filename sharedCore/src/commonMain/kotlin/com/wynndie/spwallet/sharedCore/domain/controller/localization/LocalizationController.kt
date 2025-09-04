package com.wynndie.spwallet.sharedCore.domain.controller.localization

interface LocalizationController {

    fun applyLanguage(iso: String)
    fun getLanguageIso(): String
}