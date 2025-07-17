package com.wynndie.spwallet.sharedFeature.home.domain.model.card

data class RecipientCard(
    override val id: String,
    override val name: String,
    override val color: Int,
    override val icon: Int,
    val number: String
) : Card