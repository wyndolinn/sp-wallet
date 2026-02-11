package com.wynndie.spwallet.sharedCore.presentation.models.cards

import com.wynndie.spwallet.sharedCore.domain.models.SpServersOptions
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardColors
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardIcons
import com.wynndie.spwallet.sharedCore.domain.models.cards.RecipientCard
import com.wynndie.spwallet.sharedCore.presentation.formatters.displayableValue.OreDisplayableValue

data class RecipientCardUi(
    override val id: String,
    override val server: SpServersOptions,
    override val name: String,
    override val number: String,
    override val color: CardColors,
    override val icon: CardIcons,
    override val authKey: String? = null,
    override val balance: OreDisplayableValue? = null
) : CardUi {

    fun toDomain(): RecipientCard {
        return RecipientCard(
            id = id,
            server = server,
            name = name,
            number = number,
            color = color,
            icon = icon
        )
    }

    companion object {
        fun of(value: RecipientCard): RecipientCardUi {
            return RecipientCardUi(
                id = value.id,
                server = value.server,
                name = value.name,
                number = value.number,
                color = value.color,
                icon = value.icon
            )
        }
    }
}