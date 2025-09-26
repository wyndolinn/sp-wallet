package com.wynndie.spwallet.sharedtheme.extensions

interface ComponentState {
    data object Neutral : ComponentState
    data object Disabled : ComponentState
    data object Selected : ComponentState
    data object Alerted : ComponentState
}