package com.wynndie.spwallet.sharedFeature.profile.di

import com.wynndie.spwallet.sharedFeature.profile.domain.model.AndroidLocalization
import com.wynndie.spwallet.sharedFeature.profile.domain.model.Localization
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val profilePlatformModule: Module = module {
    single<Localization> { AndroidLocalization(context = androidContext()) }
}