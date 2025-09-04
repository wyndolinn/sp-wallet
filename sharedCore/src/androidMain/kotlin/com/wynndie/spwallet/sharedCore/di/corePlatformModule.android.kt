package com.wynndie.spwallet.sharedCore.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.wynndie.spwallet.sharedCore.data.local.DataStoreFactory
import com.wynndie.spwallet.sharedCore.domain.controller.localization.AndroidLocalizationController
import com.wynndie.spwallet.sharedCore.domain.controller.localization.LocalizationController
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val corePlatformModule: Module = module {
    single<HttpClientEngine> { OkHttp.create() }
    single<DataStore<Preferences>> { DataStoreFactory(androidApplication()).create() }
    single<LocalizationController> { AndroidLocalizationController(context = androidContext()) }
}