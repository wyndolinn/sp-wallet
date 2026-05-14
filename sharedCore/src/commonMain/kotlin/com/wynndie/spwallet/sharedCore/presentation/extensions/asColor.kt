package com.wynndie.spwallet.sharedCore.presentation.extensions

import androidx.compose.ui.graphics.Color
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardColors

fun CardColors.asColor(): Color {
    return when (this) {
        CardColors.BLUE -> Color(0xFF2852C7)
        CardColors.PURPLE -> Color(0xFF9528C7)
        CardColors.PINK -> Color(0xFFC7286D)
        CardColors.RED -> Color(0xFFC72828)
        CardColors.ORANGE -> Color(0xFFC77528)
        CardColors.YELLOW -> Color(0xFFC7B728)
        CardColors.GREEN -> Color(0xFF499F49)
        CardColors.TEAL -> Color(0xFF2884C7)
    }
}