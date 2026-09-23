package com.wynndie.spwallet.sharedCore.domain.models.cards

import com.wynndie.spwallet.sharedCore.domain.models.Servers

data class CustomCard(
    val id: String,
    val server: Servers,
    val name: String,
    val balance: Long,
    val color: CardColors,
    val icon: CardIcons,
)
