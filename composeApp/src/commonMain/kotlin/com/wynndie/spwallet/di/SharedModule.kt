package com.wynndie.spwallet.di

import com.wynndie.spwallet.AppViewModel
import com.wynndie.spwallet.sharedFeature.wallet.di.walletSharedModule
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appSharedModule = module {

    viewModelOf(::AppViewModel)

    includes(
        walletSharedModule
    )
}