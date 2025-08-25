package com.wynndie.spwallet.sharedCore.presentation.model.card

import androidx.compose.runtime.Composable
import com.wynndie.spwallet.sharedCore.presentation.mapper.joinToUiText
import com.wynndie.spwallet.sharedCore.domain.model.CashCard
import com.wynndie.spwallet.sharedCore.presentation.model.displayableValue.BlocksDisplayableValue
import com.wynndie.spwallet.sharedCore.presentation.model.Tile
import com.wynndie.spwallet.sharedCore.presentation.model.UiText
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.no_name
import com.wynndie.spwallet.sharedResources.total_of_ore

data class UiCashCard(
    override val id: String,
    override val name: String,
    override val icon: CardIcon,
    override val iconBackground: CardColor,
    val balance: BlocksDisplayableValue
) : UiCard {

    fun toDomain(): CashCard {
        return CashCard(
            id = id,
            name = name,
            color = iconBackground.ordinal,
            icon = icon.ordinal,
            balance = balance.value
        )
    }

    @Composable
    fun asTile(): Tile {
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
        fun of(value: CashCard): UiCashCard {
            return UiCashCard(
                id = value.id,
                name = value.name,
                iconBackground = CardColor.from(value.color),
                icon = CardIcon.from(value.icon),
                balance = BlocksDisplayableValue.Companion.of(value.balance)
            )
        }
    }
}
