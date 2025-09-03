package com.wynndie.spwallet.sharedFeature.profile.domain.model

import android.content.Context
import android.os.LocaleList
import java.util.Locale

class AndroidLocalization(val context: Context) : Localization {

    override fun applyLanguage(iso: String) {
        val locale = Locale(iso)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocales(LocaleList(locale))
    }
}