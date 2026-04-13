package com.wynndie.spwallet.sharedFeature.transfer.di

import com.wynndie.spwallet.sharedFeature.transfer.data.repositories.TransferRepositoryImpl
import com.wynndie.spwallet.sharedFeature.transfer.domain.repositories.TransferRepository
import com.wynndie.spwallet.sharedFeature.transfer.domain.useCases.TransferByCardUseCase
import com.wynndie.spwallet.sharedFeature.transfer.domain.validators.TransferCommentValidator
import com.wynndie.spwallet.sharedFeature.transfer.presentation.RecipientViewModel
import com.wynndie.spwallet.sharedFeature.transfer.presentation.searchRecipient.SearchRecipientViewModel
import com.wynndie.spwallet.sharedFeature.transfer.presentation.transferBetweenCards.TransferBetweenCardsViewModel
import com.wynndie.spwallet.sharedFeature.transfer.presentation.transferByCard.TransferByCardViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val transferSharedModule = module {
    singleOf(::TransferRepositoryImpl).bind<TransferRepository>()

    factoryOf(::TransferByCardUseCase)
    factoryOf(::TransferCommentValidator)

    viewModelOf(::RecipientViewModel)
    viewModelOf(::SearchRecipientViewModel)
    viewModelOf(::TransferByCardViewModel)
    viewModelOf(::TransferBetweenCardsViewModel)
}