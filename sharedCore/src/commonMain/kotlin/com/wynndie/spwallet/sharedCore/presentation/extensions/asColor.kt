package com.wynndie.spwallet.sharedCore.presentation.extensions

import androidx.compose.ui.graphics.Color
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardColors

fun CardColors.asColor(): Color {
    return when (this) {
        CardColors.BLUE -> Color(0xFF2F5BF5)
        CardColors.PURPLE -> Color(0xFF7B45BF)
        CardColors.PINK -> Color(0xFFD14A7B)
        CardColors.RED -> Color(0xFFD13737)
        CardColors.ORANGE -> Color(0xFFD16415)
        CardColors.YELLOW -> Color(0xFFD19624)
        CardColors.GREEN -> Color(0xFF288949)
        CardColors.LIGHT_BLUE -> Color(0xFF2884C7)
    }
}