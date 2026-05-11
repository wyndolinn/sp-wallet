package com.wynndie.spwallet.sharedCore.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.wynndie.spwallet.sharedCore.data.database.WalletDatabaseFactory
import com.wynndie.spwallet.sharedCore.data.datastore.WalletDataStoreFactory
import com.wynndie.spwallet.sharedCore.data.network.HttpClientFactory
import com.wynndie.spwallet.sharedCore.data.repositories.CardsRepositoryImpl
import com.wynndie.spwallet.sharedCore.data.repositories.PreferencesRepositoryImpl
import com.wynndie.spwallet.sharedCore.data.repositories.RecipientRepositoryImpl
import com.wynndie.spwallet.sharedCore.data.repositories.UserRepositoryImpl
import com.wynndie.spwallet.sharedCore.domain.repositories.CardsRepository
import com.wynndie.spwallet.sharedCore.domain.repositories.PreferencesRepository
import com.wynndie.spwallet.sharedCore.domain.repositories.RecipientRepository
import com.wynndie.spwallet.sharedCore.domain.repositories.UserRepository
import com.wynndie.spwallet.sharedCore.domain.validators.BalanceValidator
import com.wynndie.spwallet.sharedCore.domain.validators.CardNameValidator
import com.wynndie.spwallet.sharedCore.domain.validators.CardNumberValidator
import com.wynndie.spwallet.sharedCore.presentation.controllers.navigation.NavEventController
import com.wynndie.spwallet.sharedCore.presentation.controllers.overlay.SnackbarController
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreSharedModule = module {
    single { HttpClientFactory.create(get()) }
    single { get<WalletDatabaseFactory>().create().setDriver(BundledSQLiteDriver()).build() }
    single { get<WalletDataStoreFactory>().create() }

    singleOf(::CardsRepositoryImpl).bind<CardsRepository>()
    singleOf(::UserRepositoryImpl).bind<UserRepository>()
    singleOf(::RecipientRepositoryImpl).bind<RecipientRepository>()
    singleOf(::PreferencesRepositoryImpl).bind<PreferencesRepository>()

    singleOf(::BalanceValidator)
    singleOf(::CardNameValidator)
    singleOf(::CardNumberValidator)

    singleOf(::NavEventController)
    singleOf(::SnackbarController)
}