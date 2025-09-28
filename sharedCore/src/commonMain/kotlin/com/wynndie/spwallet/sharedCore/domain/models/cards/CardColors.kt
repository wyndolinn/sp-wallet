package com.wynndie.spwallet.sharedCore.domain.models.cards

enum class CardColors(val id: Int) {
    BLUE(id = 0),
    PURPLE(id = 1),
    PINK(id = 2),
    RED(id = 3),
    ORANGE(id = 4),
    YELLOW(id = 5),
    GREEN(id = 6),
    LIGHT_BLUE(id = 7);

    companion object {
        fun of(id: Int) = entries.find { it.id == id } ?: BLUE
    }
}