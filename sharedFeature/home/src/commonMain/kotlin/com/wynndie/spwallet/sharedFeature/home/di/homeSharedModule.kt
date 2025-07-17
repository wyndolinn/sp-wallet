package com.wynndie.spwallet.sharedFeature.home.di

import com.wynndie.spwallet.sharedFeature.home.data.encoder.Base64AuthKeyEncoder
import com.wynndie.spwallet.sharedFeature.home.data.remote.network.KtorRemoteSpWorldsDataSource
import com.wynndie.spwallet.sharedFeature.home.data.remote.network.RemoteSpWorldsDataSource
import com.wynndie.spwallet.sharedFeature.home.data.repository.WalletRepositoryImpl
import com.wynndie.spwallet.sharedFeature.home.domain.encoder.AuthKeyEncoder
import com.wynndie.spwallet.sharedFeature.home.domain.repository.WalletRepository
import com.wynndie.spwallet.sharedFeature.home.domain.usecase.AuthCardUseCase
import com.wynndie.spwallet.sharedFeature.home.domain.usecase.DeleteAuthedCardUseCase
import com.wynndie.spwallet.sharedFeature.home.domain.usecase.SyncWithRemoteUseCase
import com.wynndie.spwallet.sharedFeature.home.domain.usecase.TransferByCardUseCase
import com.wynndie.spwallet.sharedCore.domain.validator.BalanceValidator
import com.wynndie.spwallet.sharedCore.domain.validator.CardNameValidator
import com.wynndie.spwallet.sharedCore.domain.validator.CardNumberValidator
import com.wynndie.spwallet.sharedFeature.home.domain.validator.TokenValidator
import com.wynndie.spwallet.sharedCore.domain.validator.TransferCommentValidator
import com.wynndie.spwallet.sharedFeature.home.domain.validator.UuidValidator
import com.wynndie.spwallet.sharedFeature.home.presentation.screen.cash_card.CashCardViewModel
import com.wynndie.spwallet.sharedFeature.home.presentation.screen.home.HomeViewModel
import com.wynndie.spwallet.sharedFeature.home.presentation.screen.transfer_by_card.TransferByCardViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val homeSharedModule = module {
    singleOf(::KtorRemoteSpWorldsDataSource).bind<RemoteSpWorldsDataSource>()
    singleOf(::WalletRepositoryImpl).bind<WalletRepository>()
    singleOf(::Base64AuthKeyEncoder).bind<AuthKeyEncoder>()

    singleOf(::SyncWithRemoteUseCase)
    singleOf(::AuthCardUseCase)
    singleOf(::TransferByCardUseCase)
    singleOf(::DeleteAuthedCardUseCase)

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