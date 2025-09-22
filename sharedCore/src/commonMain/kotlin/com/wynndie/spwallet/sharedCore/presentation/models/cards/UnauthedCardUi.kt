package com.wynndie.spwallet.sharedCore.presentation.models.cards

import androidx.compose.runtime.Composable
import com.wynndie.spwallet.sharedCore.domain.models.UnauthedCard
import com.wynndie.spwallet.sharedCore.presentation.models.Tile
import com.wynndie.spwallet.sharedCore.presentation.formatters.UiText
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.bank_card_label
import com.wynndie.spwallet.sharedResources.require_activation

data class UnauthedCardUi(
    override val id: String,
    override val name: String,
    override val icon: CardIcon,
    override val iconBackground: CardColor,
    val number: String,
) : CardUi {

    fun toDomain(): UnauthedCard {
        return UnauthedCard(
            id = id,
            name = name,
            color = iconBackground.ordinal,
            icon = icon.ordinal,
            number = number
        )
    }

    @Composable
    override fun asTile(): Tile {
        return Tile(
            id = id,
            icon = icon,
            iconBackground = iconBackground,
            title = UiText.ResourceString(Res.string.require_activation).asString(),
            label = UiText.ResourceString(Res.string.bank_card_label, name, number).asString()
        )
    }

    companion object {
        fun of(value: UnauthedCard): UnauthedCardUi {
            return UnauthedCardUi(
                id = value.id,
                name = value.name,
                icon = CardIcon.from(value.icon),
                iconBackground = CardColor.from(value.color),
                number = value.number
            )
        }
    }
}
