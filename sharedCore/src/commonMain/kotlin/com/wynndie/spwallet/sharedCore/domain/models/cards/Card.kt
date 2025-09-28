package com.wynndie.spwallet.sharedCore.domain.models.cards

interface Card {
    val id: String
    val authKey: String?
    val name: String
    val number: String?
    val balance: Long?
    val color: CardColors
    val icon: CardIcons
}