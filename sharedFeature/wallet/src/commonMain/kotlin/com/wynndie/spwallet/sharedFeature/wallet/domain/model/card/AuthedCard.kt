package com.wynndie.spwallet.sharedFeature.wallet.domain.model.card

data class AuthedCard(
    override val id: String,
    override val name: String,
    override val color: Int,
    override val icon: Int,
    val number: String,
    val authKey: String,
    val balance: Long
) : Card {

    fun asUnauthedCard(): UnauthedCard {
        return UnauthedCard(
            id = id,
            name = name,
            color = color,
            icon = 2,
            number = number
        )
    }
}
