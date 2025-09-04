package com.wynndie.spwallet.sharedCore.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.wynndie.spwallet.sharedCore.data.local.DataStoreFactory
import com.wynndie.spwallet.sharedCore.presentation.controller.localization.IosLocalizationController
import com.wynndie.spwallet.sharedCore.presentation.controller.localization.LocalizationController
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val corePlatformModule: Module = module {
    single<HttpClientEngine> { Darwin.create() }
    single<DataStore<Preferences>> { DataStoreFactory().create() }
    singleOf(::IosLocalizationController).bind<LocalizationController>()
}