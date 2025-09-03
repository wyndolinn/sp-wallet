package com.wynndie.spwallet.sharedFeature.profile.domain.model

import platform.Foundation.NSUserDefaults

class IosLocalization() : Localization {
    
    override fun applyLanguage(iso: String) {
        NSUserDefaults.standardUserDefaults.setObject(
            arrayListOf(iso), "AppleLanguages"
        )
    }

    override fun getLanguageIso(): String {
        return "unknown"
    }
}