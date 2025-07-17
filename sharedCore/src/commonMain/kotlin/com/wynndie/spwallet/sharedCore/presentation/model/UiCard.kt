package com.wynndie.spwallet.sharedCore.presentation.model

interface UiCard {
    val id: String
    val icon: CardIcon
    val iconBackground: CardColor
    val label: UiText
    val title: UiText
    val description: UiText?
}