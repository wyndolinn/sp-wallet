package com.wynndie.spwallet.sharedCore.domain.model

data class CashCard(
    override val id: String,
    override val name: String,
    override val color: Int,
    override val icon: Int,
    val balance: Long
) : Card
