package com.wynndie.spwallet.sharedCore.di

import com.wynndie.spwallet.sharedCore.data.database.WalletDatabaseFactory
import com.wynndie.spwallet.sharedCore.data.datastore.WalletDataStoreFactory
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual val corePlatformModule: Module = module {
    single<HttpClientEngine> { Darwin.create() }
    singleOf(::WalletDatabaseFactory)
    singleOf(::WalletDataStoreFactory)
}