package com.wynndie.spwallet.sharedFeature.home.domain.model.card

data class UnauthedCard(
    override val id: String,
    override val name: String,
    override val color: Int,
    override val icon: Int,
    val number: String,
) : Card {

    fun asAuthedCard(authKey: String, balance: Long): AuthedCard {
        return AuthedCard(
            id = id,
            name = name,
            color = color,
            icon = 1,
            number = number,
            authKey = authKey,
            balance = balance
        )
    }
}
