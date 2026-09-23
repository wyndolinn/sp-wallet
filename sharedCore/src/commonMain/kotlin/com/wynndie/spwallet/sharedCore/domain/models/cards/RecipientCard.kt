package com.wynndie.spwallet.sharedCore.domain.models.cards

import com.wynndie.spwallet.sharedCore.domain.models.Servers

data class RecipientCard(
    val id: String,
    val server: Servers,
    val name: String,
    val number: String,
    val color: CardColors,
    val icon: CardIcons,
)