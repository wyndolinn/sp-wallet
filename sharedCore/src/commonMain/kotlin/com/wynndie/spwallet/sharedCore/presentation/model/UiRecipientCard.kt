package com.wynndie.spwallet.sharedCore.presentation.model

import androidx.compose.runtime.Composable
import com.wynndie.spwallet.sharedCore.domain.model.RecipientCard
import com.wynndie.spwallet.sharedCore.presentation.model.card.CardColor
import com.wynndie.spwallet.sharedCore.presentation.model.card.CardIcon
import com.wynndie.spwallet.sharedCore.presentation.model.card.UiCard
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.enter_card_number
import com.wynndie.spwallet.sharedResources.recipient

data class UiRecipientCard(
    override val id: String,
    override val icon: CardIcon,
    override val iconBackground: CardColor,
    override val name: String,
    val number: String
) : UiCard {

    fun toDomain(): RecipientCard {
        return RecipientCard(
            id = id,
            name = name,
            color = iconBackground.ordinal,
            icon = icon.ordinal,
            number = number
        )
    }

    @Composable
    fun asTile(): Tile {
        return Tile(
            id = id,
            icon = icon,
            iconBackground = iconBackground,
            title = UiText.DynamicString(number).asString().ifBlank {
                UiText.StringResourceId(Res.string.enter_card_number).asString()
            },
            label = UiText.DynamicString(name).asString().ifBlank {
                UiText.StringResourceId(Res.string.recipient).asString()
            }
        )
    }

    companion object {
        fun of(value: RecipientCard): UiRecipientCard {
            return UiRecipientCard(
                id = value.id,
                name = value.name,
                iconBackground = CardColor.from(value.color),
                icon = CardIcon.from(value.icon),
                number = value.number
            )
        }
    }
}