package com.wynndie.spwallet.sharedCore.domain.models.cards

data class UnauthedCard(
    override val id: String,
    override val name: String,
    override val number: String,
    override val color: CardColors,
    override val icon: CardIcons,
    override val authKey: String? = null,
    override val balance: Long? = null
) : Card {

    fun asAuthedCard(authKey: String, balance: Long): AuthedCard {
        return AuthedCard(
            id = this.id,
            authKey = authKey,
            name = this.name,
            number = this.number,
            balance = balance,
            color = this.color,
            icon = CardIcons.BANK_CARD
        )
    }
}
