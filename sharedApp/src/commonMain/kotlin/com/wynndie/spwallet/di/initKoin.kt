package com.wynndie.spwallet.di

import com.wynndie.spwallet.sharedCore.di.corePlatformModule
import com.wynndie.spwallet.sharedCore.di.coreSharedModule
import com.wynndie.spwallet.sharedFeature.edit.di.editSharedModule
import com.wynndie.spwallet.sharedFeature.home.di.homeSharedModule
import com.wynndie.spwallet.sharedFeature.transfer.di.transferSharedModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            appSharedModule,
            appPlatformModule,
            coreSharedModule,
            corePlatformModule,
            homeSharedModule,
            transferSharedModule,
            editSharedModule
        )
    }
}