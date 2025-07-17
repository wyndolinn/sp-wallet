package com.wynndie.spwallet.sharedFeature.home.di

import com.wynndie.spwallet.sharedFeature.home.data.local.database.WalletDatabaseFactory
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.dsl.module

actual val walletPlatformModule: Module
    get() = module {
        single<HttpClientEngine> { OkHttp.create() }
        single { WalletDatabaseFactory(androidApplication()) }
    }