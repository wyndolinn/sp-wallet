package com.wynndie.spwallet.sharedFeature.profile.di

import com.wynndie.spwallet.sharedFeature.profile.domain.model.AndroidLocalizationController
import com.wynndie.spwallet.sharedFeature.profile.domain.model.LocalizationController
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val profilePlatformModule: Module = module {
    single<LocalizationController> { AndroidLocalizationController(context = androidContext()) }
}