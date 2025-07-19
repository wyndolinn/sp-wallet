package com.wynndie.spwallet.sharedCore.presentation.model

import com.wynndie.spwallet.sharedCore.presentation.model.card.CardColor
import com.wynndie.spwallet.sharedCore.presentation.model.card.CardIcon

data class Tile(
    val id: String,
    val icon: CardIcon,
    val iconBackground: CardColor,
    val title: String,
    val label: String? = null,
    val description: String? = null
)