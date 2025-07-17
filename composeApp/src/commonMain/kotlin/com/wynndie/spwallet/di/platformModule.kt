package com.wynndie.spwallet.di

import com.wynndie.spwallet.sharedCore.di.corePlatformModule
import org.koin.dsl.module

val platformModule = module {
    includes(
        appPlatformModule,
        corePlatformModule
    )
}