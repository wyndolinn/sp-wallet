package com.wynndie.spwallet.sharedFeature.wallet.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.wynndie.spwallet.sharedCore.data.HttpClientFactory
import com.wynndie.spwallet.sharedFeature.wallet.data.encoder.Base64AuthKeyEncoder
import com.wynndie.spwallet.sharedFeature.wallet.data.local.database.WalletDatabase
import com.wynndie.spwallet.sharedFeature.wallet.data.local.database.WalletDatabaseFactory
import com.wynndie.spwallet.sharedFeature.wallet.data.remote.network.KtorRemoteSpWorldsDataSource
import com.wynndie.spwallet.sharedFeature.wallet.data.remote.network.RemoteSpWorldsDataSource
import com.wynndie.spwallet.sharedFeature.wallet.data.repository.WalletRepositoryImpl
import com.wynndie.spwallet.sharedFeature.wallet.domain.encoder.AuthKeyEncoder
import com.wynndie.spwallet.sharedFeature.wallet.domain.repository.WalletRepository
import com.wynndie.spwallet.sharedFeature.wallet.domain.validator.BalanceValidator
import com.wynndie.spwallet.sharedFeature.wallet.domain.validator.CardNameValidator
import com.wynndie.spwallet.sharedFeature.wallet.domain.validator.CardNumberValidator
import com.wynndie.spwallet.sharedFeature.wallet.domain.validator.TokenValidator
import com.wynndie.spwallet.sharedFeature.wallet.domain.validator.TransferCommentValidator
import com.wynndie.spwallet.sharedFeature.wallet.domain.validator.UuidValidator
import com.wynndie.spwallet.sharedFeature.wallet.domain.usecase.AuthCardUseCase
import com.wynndie.spwallet.sharedFeature.wallet.domain.usecase.SyncWithRemoteUseCase
import com.wynndie.spwallet.sharedFeature.wallet.domain.usecase.TransferByCardUseCase
import com.wynndie.spwallet.sharedFeature.wallet.presentation.screen.cash_card.CashCardViewModel
import com.wynndie.spwallet.sharedFeature.wallet.presentation.screen.home.HomeViewModel
import com.wynndie.spwallet.sharedFeature.wallet.presentation.screen.transfer_by_card.TransferByCardViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val walletSharedModule = module {
    single { HttpClientFactory.create(get()) }
    singleOf(::KtorRemoteSpWorldsDataSource).bind<RemoteSpWorldsDataSource>()
    singleOf(::WalletRepositoryImpl).bind<WalletRepository>()
    singleOf(::Base64AuthKeyEncoder).bind<AuthKeyEncoder>()

    single {
        get<WalletDatabaseFactory>()
            .create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    single { get<WalletDatabase>().walletDao }

    singleOf(::SyncWithRemoteUseCase)
    singleOf(::AuthCardUseCase)
    singleOf(::TransferByCardUseCase)

    singleOf(::BalanceValidator)
    singleOf(::CardNameValidator)
    singleOf(::CardNumberValidator)
    singleOf(::TokenValidator)
    singleOf(::TransferCommentValidator)
    singleOf(::UuidValidator)

    viewModelOf(::HomeViewModel)
    viewModelOf(::CashCardViewModel)
    viewModelOf(::TransferByCardViewModel)
}