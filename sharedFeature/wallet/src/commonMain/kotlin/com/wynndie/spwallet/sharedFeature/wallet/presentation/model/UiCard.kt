package com.wynndie.spwallet.sharedFeature.wallet.presentation.model

import com.wynndie.spwallet.sharedCore.presentation.text.UiText

interface UiCard {
    val id: String
    val icon: CardIcon
    val iconBackground: CardColor
    val label: UiText
    val title: UiText
    val description: UiText?
}