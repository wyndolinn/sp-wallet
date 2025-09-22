package com.wynndie.spwallet.sharedCore.presentation.models.cards

import androidx.compose.runtime.Composable
import com.wynndie.spwallet.sharedCore.domain.models.RecipientCard
import com.wynndie.spwallet.sharedCore.presentation.models.Tile
import com.wynndie.spwallet.sharedCore.presentation.models.UiText
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.enter_card_number
import com.wynndie.spwallet.sharedResources.recipient

data class RecipientCardUi(
    override val id: String,
    override val icon: CardIcon,
    override val iconBackground: CardColor,
    override val name: String,
    val cardNumber: String
) : CardUi {

    fun toDomain(): RecipientCard {
        return RecipientCard(
            id = id,
            name = name,
            color = iconBackground.ordinal,
            icon = icon.ordinal,
            cardNumber = cardNumber
        )
    }

    @Composable
    override fun asTile(): Tile {
        return Tile(
            id = id,
            icon = icon,
            iconBackground = iconBackground,
            title = UiText.DynamicString(cardNumber).asString().ifBlank {
                UiText.StringResourceId(Res.string.enter_card_number).asString()
            },
            label = UiText.DynamicString(name).asString().ifBlank {
                UiText.StringResourceId(Res.string.recipient).asString()
            }
        )
    }

    companion object {
        fun of(value: RecipientCard): RecipientCardUi {
            return RecipientCardUi(
                id = value.id,
                name = value.name,
                iconBackground = CardColor.from(value.color),
                icon = CardIcon.from(value.icon),
                cardNumber = value.cardNumber
            )
        }
    }
}