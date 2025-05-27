package com.wynndie.spwallet.sharedFeature.wallet.domain.model.card

data class CashCard(
    override val id: String,
    override val name: String,
    override val color: Int,
    override val icon: Int,
    val balance: Long
) : Card
