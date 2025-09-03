package com.wynndie.spwallet.sharedFeature.profile.di

import com.wynndie.spwallet.sharedFeature.profile.domain.model.IosLocalizationController
import com.wynndie.spwallet.sharedFeature.profile.domain.model.LocalizationController
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val profilePlatformModule: Module = module {
    singleOf(::IosLocalizationController).bind<LocalizationController>()
}