package com.wynndie.spwallet.sharedCore.di

import com.wynndie.spwallet.sharedCore.data.remote.HttpClientFactory
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
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreSharedModule = module {
    single { HttpClientFactory.create(get()) }

    singleOf(::CardsRepositoryImpl).bind<CardsRepository>()
    singleOf(::UserRepositoryImpl).bind<UserRepository>()
    singleOf(::RecipientRepositoryImpl).bind<RecipientRepository>()
    singleOf(::PreferencesRepositoryImpl).bind<PreferencesRepository>()

    singleOf(::BalanceValidator)
    singleOf(::CardNameValidator)
    singleOf(::CardNumberValidator)
}