package com.wynndie.spwallet.sharedCore.domain.models.cards

import com.wynndie.spwallet.sharedCore.domain.models.SpServers

data class UnauthedCard(
    val id: String,
    val server: SpServers,
    val name: String,
    val number: String,
    val color: CardColors,
    val icon: CardIcons
) {
    fun asAuthedCard(authKey: String, balance: Long): AuthedCard {
        return AuthedCard(
            id = id,
            authKey = authKey,
            server = server,
            name = name,
            number = number,
            balance = balance,
            color = color,
            icon = CardIcons.BANK_CARD
        )
    }
}
