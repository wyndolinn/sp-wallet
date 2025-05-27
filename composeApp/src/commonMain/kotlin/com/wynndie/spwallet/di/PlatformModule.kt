package com.wynndie.spwallet.di

import com.wynndie.spwallet.sharedFeature.wallet.di.walletPlatformModule
import org.koin.dsl.module

val appPlatformModule = module {
    includes(
        walletPlatformModule
    )
}