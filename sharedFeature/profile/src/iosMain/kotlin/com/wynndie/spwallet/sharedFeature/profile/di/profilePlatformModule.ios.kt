package com.wynndie.spwallet.sharedFeature.profile.di

import com.wynndie.spwallet.sharedFeature.profile.domain.model.IosLocalization
import com.wynndie.spwallet.sharedFeature.profile.domain.model.Localization
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val profilePlatformModule: Module = module {
    singleOf(::IosLocalization).bind<Localization>()
}