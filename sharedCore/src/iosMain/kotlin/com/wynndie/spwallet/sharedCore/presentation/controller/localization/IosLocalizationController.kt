package com.wynndie.spwallet.sharedCore.presentation.controller.localization

import platform.Foundation.NSLocale
import platform.Foundation.NSUserDefaults
import platform.Foundation.currentLocale
import platform.Foundation.languageCode

class IosLocalizationController() : LocalizationController {

    override fun applyLanguage(iso: String) {
        NSUserDefaults.standardUserDefaults.setObject(
            arrayListOf(iso), "AppleLanguages"
        )
    }

    override fun getLanguageIso(): String {
        return NSLocale.currentLocale.languageCode
    }
}