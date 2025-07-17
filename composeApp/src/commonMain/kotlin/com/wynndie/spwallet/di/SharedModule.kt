package com.wynndie.spwallet.di

import com.wynndie.spwallet.AppViewModel
import com.wynndie.spwallet.sharedCore.di.coreSharedModule
import com.wynndie.spwallet.sharedFeature.home.di.homeSharedModule
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val sharedModule = module {

    viewModelOf(::AppViewModel)

    includes(
        appSharedModule,
        coreSharedModule,
        homeSharedModule
    )
}