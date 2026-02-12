package com.wynndie.spwallet.sharedCore.domain.models.cards

import com.wynndie.spwallet.sharedCore.domain.models.SpServers

interface Card {
    val id: String
    val server: SpServers
    val authKey: String?
    val name: String
    val number: String?
    val balance: Long?
    val color: CardColors
    val icon: CardIcons
}