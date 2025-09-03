package com.wynndie.spwallet.sharedFeature.profile.di

import com.wynndie.spwallet.sharedFeature.profile.presentation.screen.localization.ThemeViewModel
import com.wynndie.spwallet.sharedFeature.profile.presentation.screen.menu.MenuViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val profileSharedModule = module {
    viewModelOf(::MenuViewModel)
    viewModelOf(::ThemeViewModel)
}