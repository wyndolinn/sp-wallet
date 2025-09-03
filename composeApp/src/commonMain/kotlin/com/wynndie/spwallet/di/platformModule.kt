package com.wynndie.spwallet.di

import com.wynndie.spwallet.sharedCore.di.corePlatformModule
import com.wynndie.spwallet.sharedFeature.profile.di.profilePlatformModule
import org.koin.dsl.module

val platformModule = module {
    includes(
        appPlatformModule,
        corePlatformModule,
        profilePlatformModule
    )
}