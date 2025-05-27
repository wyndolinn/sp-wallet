package com.wynndie.spwallet.di

import com.wynndie.spwallet.sharedFeature.wallet.di.walletSharedModule
import org.koin.dsl.module

val appSharedModule = module {
    includes(
        walletSharedModule
    )
}