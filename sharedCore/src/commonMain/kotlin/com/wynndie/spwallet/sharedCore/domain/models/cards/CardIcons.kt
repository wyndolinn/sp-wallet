package com.wynndie.spwallet.sharedCore.domain.models.cards

enum class CardIcons(val id: Int) {
    CASH(id = 0),
    BANK_CARD(id = 1),
    ADD_CARD(id = 2),
    PERSON(id = 3);

    companion object {
        fun of(id: Int) = entries.find { it.id == id } ?: BANK_CARD
    }
}