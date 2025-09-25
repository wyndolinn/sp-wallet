package com.wynndie.spwallet.sharedCore.domain.models

enum class CardColor() {
    BLUE,
    PURPLE,
    PINK,
    RED,
    ORANGE,
    YELLOW,
    GREEN,
    LIGHT_BLUE;

    companion object {
        fun from(value: Int) = entries.getOrNull(value) ?: BLUE
    }
}