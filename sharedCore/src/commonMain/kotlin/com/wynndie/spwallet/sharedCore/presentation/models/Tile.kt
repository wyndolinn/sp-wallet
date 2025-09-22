package com.wynndie.spwallet.sharedCore.presentation.models

import com.wynndie.spwallet.sharedCore.presentation.models.cards.CardColor
import com.wynndie.spwallet.sharedCore.presentation.models.cards.CardIcon

data class Tile(
    val id: String,
    val icon: CardIcon,
    val iconBackground: CardColor,
    val title: String,
    val label: String? = null,
    val description: String? = null
)