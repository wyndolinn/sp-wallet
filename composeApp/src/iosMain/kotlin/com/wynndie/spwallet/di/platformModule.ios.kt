package com.wynndie.spwallet.di

import com.wynndie.spwallet.database.WalletDatabaseFactory
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val appPlatformModule: Module = module {
    singleOf(::WalletDatabaseFactory)
}