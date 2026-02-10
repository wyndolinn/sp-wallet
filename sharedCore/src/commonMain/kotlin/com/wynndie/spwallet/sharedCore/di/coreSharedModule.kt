package com.wynndie.spwallet.sharedCore.di

import com.wynndie.spwallet.sharedCore.data.remote.HttpClientFactory
import com.wynndie.spwallet.sharedCore.data.remote.network.KtorRemoteSpWorldsCardsDataSource
import com.wynndie.spwallet.sharedCore.data.remote.network.KtorRemoteSpWorldsUserDataSource
import com.wynndie.spwallet.sharedCore.data.remote.network.RemoteSpWorldsCardsDataSource
import com.wynndie.spwallet.sharedCore.data.remote.network.RemoteSpWorldsUserDataSource
import com.wynndie.spwallet.sharedCore.data.repositories.CardsRepositoryImpl
import com.wynndie.spwallet.sharedCore.data.repositories.RecipientRepositoryImpl
import com.wynndie.spwallet.sharedCore.data.repositories.UserRepositoryImpl
import com.wynndie.spwallet.sharedCore.domain.repositories.CardsRepository
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

    singleOf(::KtorRemoteSpWorldsUserDataSource).bind<RemoteSpWorldsUserDataSource>()
    singleOf(::KtorRemoteSpWorldsCardsDataSource).bind<RemoteSpWorldsCardsDataSource>()

    singleOf(::CardsRepositoryImpl).bind<CardsRepository>()
    singleOf(::UserRepositoryImpl).bind<UserRepository>()
    singleOf(::RecipientRepositoryImpl).bind<RecipientRepository>()

    singleOf(::BalanceValidator)
    singleOf(::CardNameValidator)
    singleOf(::CardNumberValidator)
}