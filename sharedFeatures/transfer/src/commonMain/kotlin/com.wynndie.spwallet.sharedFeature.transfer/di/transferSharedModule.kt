package com.wynndie.spwallet.sharedFeature.transfer.di

import com.wynndie.spwallet.sharedFeature.transfer.data.remote.network.KtorRemoteSpWorldsTransferDataSource
import com.wynndie.spwallet.sharedFeature.transfer.data.remote.network.RemoteSpWorldsTransferDataSource
import com.wynndie.spwallet.sharedFeature.transfer.data.repositories.TransferRepositoryImpl
import com.wynndie.spwallet.sharedFeature.transfer.domain.repositories.TransferRepository
import com.wynndie.spwallet.sharedFeature.transfer.domain.useCases.TransferByCardUseCase
import com.wynndie.spwallet.sharedFeature.transfer.presentation.screens.RecipientViewModel
import com.wynndie.spwallet.sharedFeature.transfer.presentation.screens.searchRecipient.SearchRecipientViewModel
import com.wynndie.spwallet.sharedFeature.transfer.presentation.screens.transferBetweenCards.TransferBetweenCardsViewModel
import com.wynndie.spwallet.sharedFeature.transfer.presentation.screens.transferByCard.TransferByCardViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val transferSharedModule = module {
    singleOf(::KtorRemoteSpWorldsTransferDataSource).bind<RemoteSpWorldsTransferDataSource>()
    singleOf(::TransferRepositoryImpl).bind<TransferRepository>()

    singleOf(::TransferByCardUseCase)

    viewModelOf(::RecipientViewModel)
    viewModelOf(::SearchRecipientViewModel)
    viewModelOf(::TransferByCardViewModel)
    viewModelOf(::TransferBetweenCardsViewModel)
}