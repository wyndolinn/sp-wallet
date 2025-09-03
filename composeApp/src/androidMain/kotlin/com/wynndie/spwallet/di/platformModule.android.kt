package com.wynndie.spwallet.di

import com.wynndie.spwallet.database.WalletDatabaseFactory
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.dsl.module

actual val composePlatformModule: Module = module {
    single { WalletDatabaseFactory(androidApplication()) }
}