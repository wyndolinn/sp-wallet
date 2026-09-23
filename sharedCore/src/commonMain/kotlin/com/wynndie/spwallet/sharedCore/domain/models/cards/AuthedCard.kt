package com.wynndie.spwallet.sharedCore.domain.models.cards

import com.wynndie.spwallet.sharedCore.domain.models.Servers

data class AuthedCard(
    val id: String,
    val server: Servers,
    val authKey: String,
    val name: String,
    val number: String,
    val balance: Long,
    val color: CardColors,
    val icon: CardIcons,
)
