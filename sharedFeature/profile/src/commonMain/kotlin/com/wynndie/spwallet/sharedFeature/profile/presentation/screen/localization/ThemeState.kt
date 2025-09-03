package com.wynndie.spwallet.sharedFeature.profile.presentation.screen.localization

import com.wynndie.spwallet.sharedCore.presentation.model.LoadingState
import com.wynndie.spwallet.sharedFeature.profile.presentation.model.Languages
import kotlin.enums.EnumEntries

data class ThemeState(
    val loadingState: LoadingState = LoadingState.Finished,
    val selectedAppTheme: String = "",
    val selectedLanguageCode: String = ""
)
