package com.wynndie.spwallet.sharedFeature.home.presentation.model

import com.wynndie.spwallet.sharedCore.presentation.model.UiText

interface UiCard {
    val id: String
    val icon: CardIcon
    val iconBackground: CardColor
    val label: UiText
    val title: UiText
    val description: UiText?
}