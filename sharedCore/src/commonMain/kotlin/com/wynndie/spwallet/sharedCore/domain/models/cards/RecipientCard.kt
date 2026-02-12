package com.wynndie.spwallet.sharedCore.domain.models.cards

import com.wynndie.spwallet.sharedCore.domain.models.SpServers

data class RecipientCard(
    override val id: String,
    override val server: SpServers,
    override val name: String,
    override val number: String,
    override val color: CardColors,
    override val icon: CardIcons,
    override val authKey: String? = null,
    override val balance: Long? = null
) : Card