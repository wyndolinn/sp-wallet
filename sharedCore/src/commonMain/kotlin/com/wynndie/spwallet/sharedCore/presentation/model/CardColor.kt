package com.wynndie.spwallet.sharedCore.presentation.model

import androidx.compose.ui.graphics.Color

enum class CardColor(val value: Color) {
    Blue(Color(0xFF2F5BF5)),
    Purple(Color(0xFF7B45BF)),
    Pink(Color(0xFFD14A7B)),
    Red(Color(0xFFD13737)),
    Orange(Color(0xFFD16415)),
    Yellow(Color(0xFFD19624)),
    Green(Color(0xFF288949)),
    LightBlue(Color(0xFF2884C7));

    companion object {
        fun from(value: Int) = entries.getOrNull(value) ?: Blue
    }
}