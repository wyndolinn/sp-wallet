package com.wynndie.spwallet.sharedFeature.profile.presentation.screen.localization

sealed interface ThemeAction {
    data object OnClickBack : ThemeAction
    data class OnClickLanguage(val code: String) : ThemeAction
}