package com.wynndie.spwallet.di

import com.wynndie.spwallet.database.WalletDatabaseFactory
import com.wynndie.spwallet.datastore.WalletDataStoreFactory
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.dsl.module

actual val appPlatformModule: Module = module {
    single { WalletDatabaseFactory(androidApplication()) }
    single { WalletDataStoreFactory(androidApplication()) }
}