package com.wynndie.spwallet.sharedCore.domain.constructors

import com.wynndie.spwallet.sharedCore.domain.models.cards.AuthedCard
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardColors
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardIcons

fun createAuthedCard(
    name: String,
    number: String,
    balance: Long,
    id: String = "",
    authKey: String = "",
    colors: CardColors = CardColors.BLUE,
    icon: CardIcons = CardIcons.BANK_CARD
): AuthedCard {
    return AuthedCard(
        id = id,
        authKey = authKey,
        name = name,
        number = number,
        balance = balance,
        color = colors,
        icon = icon
    )
}