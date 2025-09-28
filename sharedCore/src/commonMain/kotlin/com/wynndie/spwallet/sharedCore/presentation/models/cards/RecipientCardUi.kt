package com.wynndie.spwallet.sharedCore.presentation.models.cards

import com.wynndie.spwallet.sharedCore.domain.models.cards.CardColors
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardIcons
import com.wynndie.spwallet.sharedCore.domain.models.cards.RecipientCard
import com.wynndie.spwallet.sharedCore.presentation.formatters.displayableValue.OreDisplayableValue

data class RecipientCardUi(
    override val id: String,
    override val name: String,
    override val number: String,
    override val color: CardColors,
    override val icon: CardIcons,
    override val authKey: String? = null,
    override val balance: OreDisplayableValue? = null
) : CardUi {

    fun toDomain(): RecipientCard {
        return RecipientCard(
            id = this.id,
            name = this.name,
            number = this.number,
            color = this.color,
            icon = this.icon
        )
    }

    companion object {
        fun of(value: RecipientCard): RecipientCardUi {
            return RecipientCardUi(
                id = value.id,
                name = value.name,
                number = value.number,
                color = value.color,
                icon = value.icon
            )
        }
    }
}