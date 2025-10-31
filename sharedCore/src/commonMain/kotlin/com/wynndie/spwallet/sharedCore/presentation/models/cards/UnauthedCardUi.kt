package com.wynndie.spwallet.sharedCore.presentation.models.cards

import com.wynndie.spwallet.sharedCore.domain.models.cards.CardColors
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardIcons
import com.wynndie.spwallet.sharedCore.domain.models.cards.UnauthedCard
import com.wynndie.spwallet.sharedCore.presentation.formatters.displayableValue.OreDisplayableValue
import kotlinx.serialization.Serializable

data class UnauthedCardUi(
    override val id: String,
    override val name: String,
    override val number: String,
    override val color: CardColors,
    override val icon: CardIcons,
    override val authKey: String? = null,
    override val balance: OreDisplayableValue? = null
) : CardUi {

    fun toDomain(): UnauthedCard {
        return UnauthedCard(
            id = this.id,
            name = this.name,
            number = this.number,
            color = this.color,
            icon = this.icon
        )
    }

    companion object {
        fun of(value: UnauthedCard): UnauthedCardUi {
            return UnauthedCardUi(
                id = value.id,
                name = value.name,
                number = value.number,
                color = value.color,
                icon = value.icon
            )
        }
    }
}
