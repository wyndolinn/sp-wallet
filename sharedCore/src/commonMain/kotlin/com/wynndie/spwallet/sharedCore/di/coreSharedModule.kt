package com.wynndie.spwallet.sharedCore.di

import com.wynndie.spwallet.sharedCore.data.HttpClientFactory
import com.wynndie.spwallet.sharedCore.domain.validator.BalanceValidator
import com.wynndie.spwallet.sharedCore.domain.validator.CardNameValidator
import com.wynndie.spwallet.sharedCore.domain.validator.CardNumberValidator
import com.wynndie.spwallet.sharedCore.domain.validator.TransferCommentValidator
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val coreSharedModule = module {
    single { HttpClientFactory.create(get()) }

    singleOf(::BalanceValidator)
    singleOf(::CardNameValidator)
    singleOf(::CardNumberValidator)
    singleOf(::TransferCommentValidator)
}