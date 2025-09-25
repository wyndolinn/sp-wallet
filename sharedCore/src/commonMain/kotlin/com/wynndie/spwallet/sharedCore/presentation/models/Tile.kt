package com.wynndie.spwallet.sharedCore.presentation.models

import com.wynndie.spwallet.sharedCore.domain.models.CardColor
import com.wynndie.spwallet.sharedCore.domain.models.CardIcon

data class Tile(
    val id: String,
    val icon: CardIcon,
    val iconBackground: CardColor,
    val title: String,
    val label: String? = null,
    val description: String? = null
)