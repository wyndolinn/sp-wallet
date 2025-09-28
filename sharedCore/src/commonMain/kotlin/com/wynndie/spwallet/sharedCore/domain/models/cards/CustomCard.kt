package com.wynndie.spwallet.sharedCore.domain.models.cards

data class CustomCard(
    override val id: String,
    override val name: String,
    override val balance: Long,
    override val color: CardColors,
    override val icon: CardIcons,
    override val authKey: String? = null,
    override val number: String? = null
) : Card
