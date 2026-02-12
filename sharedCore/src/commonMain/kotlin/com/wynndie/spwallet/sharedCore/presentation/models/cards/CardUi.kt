package com.wynndie.spwallet.sharedCore.presentation.models.cards

import com.wynndie.spwallet.sharedCore.domain.models.SpServers
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardColors
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardIcons
import com.wynndie.spwallet.sharedCore.presentation.formatters.displayableValue.OreDisplayableValue

interface CardUi {
    val id: String
    val authKey: String?
    val server: SpServers
    val name: String
    val number: String?
    val balance: OreDisplayableValue?
    val color: CardColors
    val icon: CardIcons
}