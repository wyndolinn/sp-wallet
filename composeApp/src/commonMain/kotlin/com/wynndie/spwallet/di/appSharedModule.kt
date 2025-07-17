package com.wynndie.spwallet.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.wynndie.spwallet.database.WalletDatabase
import com.wynndie.spwallet.database.WalletDatabaseFactory
import org.koin.dsl.module

val appSharedModule = module {
    single {
        get<WalletDatabaseFactory>()
            .create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }

    single { get<WalletDatabase>().walletDao }
}