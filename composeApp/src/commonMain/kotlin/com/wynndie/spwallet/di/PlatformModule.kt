package com.wynndie.spwallet.di

import com.wynndie.spwallet.sharedFeature.home.di.walletPlatformModule
import org.koin.dsl.module

val appPlatformModule = module {
    includes(
        walletPlatformModule
    )
}