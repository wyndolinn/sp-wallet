package com.wynndie.spwallet.sharedCore.presentation.extensions

import androidx.compose.ui.graphics.Color
import com.wynndie.spwallet.sharedCore.domain.models.CardColor

fun CardColor.asColor(): Color {
    return when (this) {
        CardColor.BLUE -> Color(0xFF2F5BF5)
        CardColor.PURPLE -> Color(0xFF7B45BF)
        CardColor.PINK -> Color(0xFFD14A7B)
        CardColor.RED -> Color(0xFFD13737)
        CardColor.ORANGE -> Color(0xFFD16415)
        CardColor.YELLOW -> Color(0xFFD19624)
        CardColor.GREEN -> Color(0xFF288949)
        CardColor.LIGHT_BLUE -> Color(0xFF2884C7)
    }
}