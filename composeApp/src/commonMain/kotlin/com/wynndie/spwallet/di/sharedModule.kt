package com.wynndie.spwallet.di

import com.wynndie.spwallet.sharedCore.di.coreSharedModule
import com.wynndie.spwallet.sharedFeature.edit.di.editSharedModule
import com.wynndie.spwallet.sharedFeature.home.di.homeSharedModule
import com.wynndie.spwallet.sharedFeature.transfer.di.transferSharedModule
import org.koin.dsl.module

val sharedModule = module {
    includes(
        appSharedModule,
        coreSharedModule,
        homeSharedModule,
        transferSharedModule,
        editSharedModule
    )
}