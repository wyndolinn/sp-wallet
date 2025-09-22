package com.wynndie.spwallet.sharedCore.presentation.model.card

import androidx.compose.runtime.Composable
import com.wynndie.spwallet.sharedCore.presentation.model.Tile

interface UiCard {
    val id: String
    val name: String
    val icon: CardIcon
    val iconBackground: CardColor

    @Composable
    fun asTile(): Tile
}