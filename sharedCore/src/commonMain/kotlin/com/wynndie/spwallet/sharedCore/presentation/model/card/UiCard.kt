package com.wynndie.spwallet.sharedCore.presentation.model.card

interface UiCard {
    val id: String
    val name: String
    val icon: CardIcon
    val iconBackground: CardColor
}