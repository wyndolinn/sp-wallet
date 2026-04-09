package com.wynndie.spwallet.sharedCore.presentation.models.cards

import com.wynndie.spwallet.sharedCore.domain.models.SpServers
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardColors
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardIcons
import com.wynndie.spwallet.sharedCore.domain.models.cards.CustomCard
import com.wynndie.spwallet.sharedCore.presentation.formatters.displayableValue.OreDisplayableValue

data class CustomCardUi(
    override val id: String,
    override val server: SpServers,
    override val name: String,
    override val balance: OreDisplayableValue,
    override val color: CardColors,
    override val icon: CardIcons,
    override val authKey: String? = null,
    override val number: String? = null
) : CardUi {

    fun toDomain(): CustomCard {
        return CustomCard(
            id = id,
            server = server,
            name = name,
            balance = balance.value,
            color = color,
            icon = icon
        )
    }

    companion object {
        fun of(value: CustomCard): CustomCardUi {
            return CustomCardUi(
                id = value.id,
                server = value.server,
                name = value.name,
                balance = OreDisplayableValue.of(value.balance),
                color = value.color,
                icon = value.icon
            )
        }
    }
}
