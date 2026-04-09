package com.wynndie.spwallet.sharedFeature.home.di

import com.wynndie.spwallet.sharedFeature.home.domain.encoders.AuthKeyEncoder
import com.wynndie.spwallet.sharedFeature.home.domain.encoders.Base64AuthKeyEncoder
import com.wynndie.spwallet.sharedFeature.home.domain.useCases.AuthCardUseCase
import com.wynndie.spwallet.sharedFeature.home.domain.useCases.DeleteAuthedCardUseCase
import com.wynndie.spwallet.sharedFeature.home.domain.useCases.SyncWithRemoteUseCase
import com.wynndie.spwallet.sharedFeature.home.domain.validators.TokenValidator
import com.wynndie.spwallet.sharedFeature.home.domain.validators.UuidValidator
import com.wynndie.spwallet.sharedFeature.home.presentation.screens.home.HomeViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val homeSharedModule = module {
    singleOf(::Base64AuthKeyEncoder).bind<AuthKeyEncoder>()

    singleOf(::SyncWithRemoteUseCase)
    singleOf(::AuthCardUseCase)
    singleOf(::DeleteAuthedCardUseCase)

    singleOf(::TokenValidator)
    singleOf(::UuidValidator)

    viewModelOf(::HomeViewModel)
}