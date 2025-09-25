package com.wynndie.spwallet.sharedCore.presentation.models.cards

import androidx.compose.runtime.Composable
import com.wynndie.spwallet.sharedCore.domain.models.CardColor
import com.wynndie.spwallet.sharedCore.domain.models.CardIcon
import com.wynndie.spwallet.sharedCore.presentation.models.Tile

interface CardUi {
    val id: String
    val name: String
    val icon: CardIcon
    val iconBackground: CardColor

    @Composable
    fun asTile(): Tile
}