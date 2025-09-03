package com.wynndie.spwallet.sharedCore.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.wynndie.spwallet.sharedCore.data.local.DataStoreFactory
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.module.Module
import org.koin.dsl.module

actual val corePlatformModule: Module = module {
    single<HttpClientEngine> { Darwin.create() }
    single<DataStore<Preferences>> { DataStoreFactory().create() }
}