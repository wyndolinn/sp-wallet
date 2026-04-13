package com.wynndie.spwallet.sharedCore.domain.models.cards

import com.wynndie.spwallet.sharedCore.domain.models.SpServers

data class RecipientCard(
    val id: String,
    val server: SpServers,
    val name: String,
    val number: String,
    val color: CardColors,
    val icon: CardIcons
)