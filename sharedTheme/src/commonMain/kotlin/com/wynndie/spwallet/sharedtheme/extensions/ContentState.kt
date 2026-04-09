package com.wynndie.spwallet.sharedtheme.extensions

sealed interface ContentState {
    data object Neutral : ContentState
    data object Disabled : ContentState
    data object Selected : ContentState
    data object Alerted : ContentState
}