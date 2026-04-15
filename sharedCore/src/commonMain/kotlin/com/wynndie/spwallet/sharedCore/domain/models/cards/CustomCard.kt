package com.wynndie.spwallet.sharedCore.domain.models.cards

import com.wynndie.spwallet.sharedCore.domain.models.SpServers

data class CustomCard(
    val id: String,
    val server: SpServers,
    val name: String,
    val balance: Long,
    val color: CardColors,
    val icon: CardIcons,
)
