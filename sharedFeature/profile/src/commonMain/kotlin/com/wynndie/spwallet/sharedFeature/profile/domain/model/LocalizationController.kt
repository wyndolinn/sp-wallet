package com.wynndie.spwallet.sharedFeature.profile.domain.model

interface LocalizationController {

    fun applyLanguage(iso: String)
    fun getLanguageIso(): String
}