package com.wynndie.spwallet.sharedFeature.profile.presentation.screen.menu

import com.wynndie.spwallet.sharedCore.domain.model.AuthedUser
import com.wynndie.spwallet.sharedCore.presentation.model.LoadingState
import com.wynndie.spwallet.sharedCore.presentation.model.emptyAuthedUser

data class MenuState(
    val loadingState: LoadingState  = LoadingState.Finished,
    val user: AuthedUser = emptyAuthedUser
)
