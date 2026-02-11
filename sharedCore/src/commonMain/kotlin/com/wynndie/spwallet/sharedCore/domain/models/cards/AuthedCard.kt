package com.wynndie.spwallet.sharedCore.domain.models.cards

import com.wynndie.spwallet.sharedCore.domain.models.SpServersOptions

data class AuthedCard(
    override val id: String,
    override val server: SpServersOptions,
    override val authKey: String,
    override val name: String,
    override val number: String,
    override val balance: Long,
    override val color: CardColors,
    override val icon: CardIcons
) : Card {

    fun asUnauthedCard(): UnauthedCard {
        return UnauthedCard(
            id = this.id,
            authKey = "",
            server = this.server,
            name = this.name,
            number = this.number,
            balance = 0L,
            color = this.color,
            icon = CardIcons.ADD_CARD
        )
    }
}
