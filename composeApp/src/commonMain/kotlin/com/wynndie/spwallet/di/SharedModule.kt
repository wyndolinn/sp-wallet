package com.wynndie.spwallet.di

import com.wynndie.spwallet.AppViewModel
import com.wynndie.spwallet.sharedFeature.wallet.di.walletSharedModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appSharedModule = module {

    singleOf(::AppViewModel)

    includes(
        walletSharedModule
    )
}