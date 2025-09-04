package com.wynndie.spwallet.sharedCore.domain.controller.localization

import platform.Foundation.NSUserDefaults

class IosLocalizationController() : LocalizationController {

    override fun applyLanguage(iso: String) {
        NSUserDefaults.standardUserDefaults.setObject(
            arrayListOf(iso), "AppleLanguages"
        )
    }

    override fun getLanguageIso(): String {
        return "unknown"
    }
}