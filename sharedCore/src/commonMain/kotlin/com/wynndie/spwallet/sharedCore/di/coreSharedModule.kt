package com.wynndie.spwallet.sharedCore.di

import com.wynndie.spwallet.sharedCore.data.remote.HttpClientFactory
import com.wynndie.spwallet.sharedCore.data.remote.network.KtorRemoteSpWorldsCardsDataSource
import com.wynndie.spwallet.sharedCore.data.remote.network.KtorRemoteSpWorldsUserDataSource
import com.wynndie.spwallet.sharedCore.data.remote.network.RemoteSpWorldsCardsDataSource
import com.wynndie.spwallet.sharedCore.data.remote.network.RemoteSpWorldsUserDataSource
import com.wynndie.spwallet.sharedCore.data.repository.CardsRepositoryImpl
import com.wynndie.spwallet.sharedCore.data.repository.DataStoreRepositoryImpl
import com.wynndie.spwallet.sharedCore.data.repository.RecipientRepositoryImpl
import com.wynndie.spwallet.sharedCore.data.repository.UserRepositoryImpl
import com.wynndie.spwallet.sharedCore.domain.repository.CardsRepository
import com.wynndie.spwallet.sharedCore.domain.repository.DataStoreRepository
import com.wynndie.spwallet.sharedCore.domain.repository.RecipientRepository
import com.wynndie.spwallet.sharedCore.domain.repository.UserRepository
import com.wynndie.spwallet.sharedCore.domain.validator.BalanceValidator
import com.wynndie.spwallet.sharedCore.domain.validator.CardNameValidator
import com.wynndie.spwallet.sharedCore.domain.validator.CardNumberValidator
import com.wynndie.spwallet.sharedCore.domain.validator.TransferCommentValidator
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
    singleOf(::DataStoreRepositoryImpl).bind<DataStoreRepository>()

    singleOf(::BalanceValidator)
    singleOf(::CardNameValidator)
    singleOf(::CardNumberValidator)
    singleOf(::TransferCommentValidator)
}