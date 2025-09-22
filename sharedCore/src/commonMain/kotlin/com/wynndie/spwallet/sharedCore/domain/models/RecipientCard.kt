package com.wynndie.spwallet.sharedCore.domain.models

data class RecipientCard(
    override val id: String,
    override val name: String,
    override val color: Int,
    override val icon: Int,
    val cardNumber: String
) : Card