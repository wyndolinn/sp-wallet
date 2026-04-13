package com.wynndie.spwallet.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.wynndie.spwallet.AppViewModel
import com.wynndie.spwallet.database.WalletDatabase
import com.wynndie.spwallet.database.WalletDatabaseFactory
import com.wynndie.spwallet.datastore.WalletDataStoreFactory
import com.wynndie.spwallet.sharedCore.data.remote.HttpClientFactory
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appSharedModule = module {
    single { get<WalletDatabaseFactory>().create().setDriver(BundledSQLiteDriver()).build() }
    single { get<WalletDataStoreFactory>().create() }

    single { get<WalletDatabase>().userDao }
    single { get<WalletDatabase>().cardsDao }
    single { get<WalletDatabase>().recipientDao }

    viewModelOf(::AppViewModel)
}