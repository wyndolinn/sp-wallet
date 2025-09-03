package com.wynndie.spwallet.sharedFeature.profile.presentation.screen.localization

import com.wynndie.spwallet.sharedCore.presentation.model.LoadingState

data class ThemeState(
    val loadingState: LoadingState = LoadingState.Finished,
    val selectedAppTheme: String = "",
    val selectedLanguageIso: String = ""
)
