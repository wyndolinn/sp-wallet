package com.wynndie.spwallet.sharedCore.presentation.model.card

import androidx.compose.runtime.Composable
import com.wynndie.spwallet.sharedCore.presentation.mapper.joinToUiText
import com.wynndie.spwallet.sharedCore.domain.model.AuthedCard
import com.wynndie.spwallet.sharedCore.presentation.model.BlocksDisplayableValue
import com.wynndie.spwallet.sharedCore.presentation.model.Tile
import com.wynndie.spwallet.sharedCore.presentation.model.UiText
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.bank_card_label
import com.wynndie.spwallet.sharedResources.total_of_ore

data class UiAuthedCard(
    override val id: String,
    override val name: String,
    override val icon: CardIcon,
    override val iconBackground: CardColor,
    val authKey: String,
    val number: String,
    val balance: BlocksDisplayableValue
) : UiCard {

    fun toDomain(): AuthedCard {
        return AuthedCard(
            id = id,
            name = name,
            color = iconBackground.ordinal,
            icon = icon.ordinal,
            number = number,
            authKey = authKey,
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
            label = UiText.StringResourceId(Res.string.bank_card_label, name, number).asString(),
            description = balance.formatted.joinToUiText(" ").asString()
        )
    }

    companion object {
        fun of(value: AuthedCard): UiAuthedCard {
            return UiAuthedCard(
                id = value.id,
                name = value.name,
                icon = CardIcon.from(value.icon),
                iconBackground = CardColor.from(value.color),
                number = value.number,
                authKey = value.authKey,
                balance = BlocksDisplayableValue.Companion.of(value.balance)
            )
        }
    }
}
