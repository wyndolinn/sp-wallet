package com.wynndie.spwallet.sharedCore.di

import com.wynndie.spwallet.sharedCore.data.database.WalletDatabaseFactory
import com.wynndie.spwallet.sharedCore.data.datastore.WalletDataStoreFactory
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.dsl.module

actual val corePlatformModule: Module = module {
    single<HttpClientEngine> { OkHttp.create() }
    single { WalletDatabaseFactory(androidApplication()) }
    single { WalletDataStoreFactory(androidApplication()) }
}