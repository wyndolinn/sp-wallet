package com.wynndie.spwallet.sharedFeature.profile.presentation.screen.menu

sealed interface MenuAction {
    data object OnClickBack : MenuAction
    data object OnClickTheme : MenuAction
}