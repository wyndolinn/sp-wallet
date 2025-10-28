package com.wynndie.spwallet.sharedCore.presentation.models.cards

import com.wynndie.spwallet.sharedCore.domain.models.cards.AuthedCard
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardColors
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardIcons
import com.wynndie.spwallet.sharedCore.presentation.formatters.displayableValue.OreDisplayableValue
import kotlinx.serialization.Serializable

@Serializable
data class AuthedCardUi(
    override val id: String,
    override val authKey: String,
    override val name: String,
    override val number: String,
    override val balance: OreDisplayableValue,
    override val color: CardColors,
    override val icon: CardIcons
) : CardUi {

    fun toDomain(): AuthedCard {
        return AuthedCard(
            id = this.id,
            authKey = this.authKey,
            name = this.name,
            number = this.number,
            balance = this.balance.value,
            color = this.color,
            icon = this.icon
        )
    }

    companion object {
        fun of(value: AuthedCard): AuthedCardUi {
            return AuthedCardUi(
                id = value.id,
                authKey = value.authKey,
                name = value.name,
                number = value.number,
                balance = OreDisplayableValue.of(value.balance),
                color = value.color,
                icon = value.icon
            )
        }
    }
}
