package com.wynndie.spwallet.sharedFeature.profile.domain.model

import android.content.Context
import android.os.LocaleList
import java.util.Locale

class AndroidLocalizationController(val context: Context) : LocalizationController {

    override fun applyLanguage(iso: String) {
        val locale = Locale(iso)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocales(LocaleList(locale))
    }

    override fun getLanguageIso(): String {
        return Locale.getDefault().language
    }
}