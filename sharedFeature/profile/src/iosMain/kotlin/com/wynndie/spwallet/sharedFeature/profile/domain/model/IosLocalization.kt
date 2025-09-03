package com.wynndie.spwallet.sharedFeature.profile.domain.model

class IosLocalization() : Localization {
    
    override fun applyLanguage(iso: String) {
        NSUserDefaults.standardUserDefaults.setObject(
            arrayListOf(iso), "AppleLanguages"
        )
    }
}