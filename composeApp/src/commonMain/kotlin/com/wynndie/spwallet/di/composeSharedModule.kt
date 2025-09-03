package com.wynndie.spwallet.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.wynndie.spwallet.AppViewModel
import com.wynndie.spwallet.database.WalletDatabase
import com.wynndie.spwallet.database.WalletDatabaseFactory
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val composeSharedModule = module {
    single {
        get<WalletDatabaseFactory>()
            .create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }

    single { get<WalletDatabase>().walletDao }
    single { get<WalletDatabase>().userDao }
    single { get<WalletDatabase>().cardsDao }
    single { get<WalletDatabase>().recipientDao }

    viewModelOf(::AppViewModel)
}