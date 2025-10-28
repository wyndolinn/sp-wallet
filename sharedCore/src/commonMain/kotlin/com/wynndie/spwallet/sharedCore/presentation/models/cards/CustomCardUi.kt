package com.wynndie.spwallet.sharedCore.presentation.models.cards

import com.wynndie.spwallet.sharedCore.domain.models.cards.CardColors
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardIcons
import com.wynndie.spwallet.sharedCore.domain.models.cards.CustomCard
import com.wynndie.spwallet.sharedCore.presentation.formatters.displayableValue.OreDisplayableValue
import kotlinx.serialization.Serializable

@Serializable
data class CustomCardUi(
    override val id: String,
    override val name: String,
    override val balance: OreDisplayableValue,
    override val color: CardColors,
    override val icon: CardIcons,
    override val authKey: String? = null,
    override val number: String? = null
) : CardUi {

    fun toDomain(): CustomCard {
        return CustomCard(
            id = this.id,
            name = this.name,
            balance = this.balance.value,
            color = this.color,
            icon = this.icon
        )
    }

    companion object {
        fun of(value: CustomCard): CustomCardUi {
            return CustomCardUi(
                id = value.id,
                name = value.name,
                balance = OreDisplayableValue.of(value.balance),
                color = value.color,
                icon = value.icon
            )
        }
    }
}
