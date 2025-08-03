package com.wynndie.spwallet.sharedFeature.transfer.di

import com.wynndie.spwallet.sharedFeature.transfer.data.remote.network.KtorRemoteSpWorldsTransferDataSource
import com.wynndie.spwallet.sharedFeature.transfer.data.remote.network.RemoteSpWorldsTransferDataSource
import com.wynndie.spwallet.sharedFeature.transfer.data.repository.TransferRepositoryImpl
import com.wynndie.spwallet.sharedFeature.transfer.domain.repository.TransferRepository
import com.wynndie.spwallet.sharedFeature.transfer.domain.usecase.TransferByCardUseCase
import com.wynndie.spwallet.sharedFeature.transfer.presentation.screen.searchRecipient.SearchRecipientViewModel
import com.wynndie.spwallet.sharedFeature.transfer.presentation.screen.transferByCard.TransferByCardViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val transferSharedModule = module {
    singleOf(::KtorRemoteSpWorldsTransferDataSource).bind<RemoteSpWorldsTransferDataSource>()
    singleOf(::TransferRepositoryImpl).bind<TransferRepository>()

    singleOf(::TransferByCardUseCase)

    viewModelOf(::SearchRecipientViewModel)
    viewModelOf(::TransferByCardViewModel)
}