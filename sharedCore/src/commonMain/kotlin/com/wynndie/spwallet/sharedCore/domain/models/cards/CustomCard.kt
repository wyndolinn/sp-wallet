package com.wynndie.spwallet.sharedCore.domain.models.cards

import com.wynndie.spwallet.sharedCore.domain.models.SpServersOptions

data class CustomCard(
    override val id: String,
    override val server: SpServersOptions,
    override val name: String,
    override val balance: Long,
    override val color: CardColors,
    override val icon: CardIcons,
    override val authKey: String? = null,
    override val number: String? = null
) : Card
