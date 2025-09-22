package com.wynndie.spwallet.sharedCore.presentation.models.cards

import androidx.compose.runtime.Composable
import com.wynndie.spwallet.sharedCore.presentation.extensions.joinToUiText
import com.wynndie.spwallet.sharedCore.domain.models.AuthedCard
import com.wynndie.spwallet.sharedCore.presentation.formatters.displayableValue.BlocksDisplayableValue
import com.wynndie.spwallet.sharedCore.presentation.models.Tile
import com.wynndie.spwallet.sharedCore.presentation.formatters.UiText
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.bank_card_label
import com.wynndie.spwallet.sharedResources.total_of_ore

data class AuthedCardUi(
    override val id: String,
    override val name: String,
    override val icon: CardIcon,
    override val iconBackground: CardColor,
    val authKey: String,
    val number: String,
    val balance: BlocksDisplayableValue
) : CardUi {

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
    override fun asTile(): Tile {
        return Tile(
            id = id,
            icon = icon,
            iconBackground = iconBackground,
            title = UiText.ResourceString(Res.string.total_of_ore, balance.value).asString(),
            label = UiText.ResourceString(Res.string.bank_card_label, name, number).asString(),
            description = balance.formatted.joinToUiText(" ").asString()
        )
    }

    companion object {
        fun of(value: AuthedCard): AuthedCardUi {
            return AuthedCardUi(
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
