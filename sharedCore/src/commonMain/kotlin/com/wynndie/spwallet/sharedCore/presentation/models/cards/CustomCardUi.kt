package com.wynndie.spwallet.sharedCore.presentation.models.cards

import androidx.compose.runtime.Composable
import com.wynndie.spwallet.sharedCore.presentation.extensions.joinToUiText
import com.wynndie.spwallet.sharedCore.domain.models.CustomCard
import com.wynndie.spwallet.sharedCore.presentation.formatters.displayableValue.BlocksDisplayableValue
import com.wynndie.spwallet.sharedCore.presentation.models.Tile
import com.wynndie.spwallet.sharedCore.presentation.models.UiText
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.no_name
import com.wynndie.spwallet.sharedResources.total_of_ore

data class CustomCardUi(
    override val id: String,
    override val name: String,
    override val icon: CardIcon,
    override val iconBackground: CardColor,
    val balance: BlocksDisplayableValue
) : CardUi {

    fun toDomain(): CustomCard {
        return CustomCard(
            id = id,
            name = name,
            color = iconBackground.ordinal,
            icon = icon.ordinal,
            balance = balance.value
        )
    }

    @Composable
    override fun asTile(): Tile {
        return Tile(
            id = id,
            icon = icon,
            iconBackground = iconBackground,
            title = UiText.StringResourceId(Res.string.total_of_ore, balance.value).asString(),
            label = UiText.DynamicString(name).asString().ifBlank {
                UiText.StringResourceId(Res.string.no_name).asString()
            },
            description = balance.formatted.joinToUiText(" ").asString()
        )
    }

    companion object {
        fun of(value: CustomCard): CustomCardUi {
            return CustomCardUi(
                id = value.id,
                name = value.name,
                iconBackground = CardColor.from(value.color),
                icon = CardIcon.from(value.icon),
                balance = BlocksDisplayableValue.Companion.of(value.balance)
            )
        }
    }
}
