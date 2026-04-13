package com.wynndie.spwallet.sharedFeature.edit.di

import com.wynndie.spwallet.sharedFeature.edit.presentation.CustomCardViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val editSharedModule: Module = module {
    viewModelOf(::CustomCardViewModel)
}