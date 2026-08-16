package com.wynndie.spwallet.sharedCore.presentation.extensions

import androidx.compose.ui.graphics.Color
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardColors

fun CardColors.asColor(): Color {
    return when (this) {
        CardColors.BLUE -> Color(0xFF5968C7)
        CardColors.PURPLE -> Color(0xFFBE59C7)
        CardColors.PINK -> Color(0xFFC75992)
        CardColors.RED -> Color(0xFFC7595B)
        CardColors.ORANGE -> Color(0xFFC77A59)
        CardColors.YELLOW -> Color(0xFFC7C359)
        CardColors.GREEN -> Color(0xFF59C76A)
        CardColors.TEAL -> Color(0xFF59A6C7)
    }
}