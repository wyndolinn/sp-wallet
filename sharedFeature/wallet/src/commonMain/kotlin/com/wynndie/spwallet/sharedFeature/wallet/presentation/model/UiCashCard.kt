package com.wynndie.spwallet.sharedFeature.wallet.presentation.model

import androidx.compose.runtime.Composable
import com.wynndie.spwallet.sharedCore.presentation.mapper.joinAsString
import com.wynndie.spwallet.sharedCore.presentation.model.UiText
import com.wynndie.spwallet.sharedFeature.wallet.domain.model.card.CashCard
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.total_of_ore

data class UiCashCard(
    override val id: String,
    override val icon: CardIcon,
    override val iconBackground: CardColor,
    override val label: UiText,
    override val title: UiText,
    override val description: UiText? = null,
    val name: String,
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
    fun formatDescription(): UiCashCard {
        return this.copy(
            description = UiText.DynamicString(
                balance.formatted.joinAsString(" ")
            )
        )
    }

    companion object {
        fun of(value: CashCard): UiCashCard {
            val balance = BlocksDisplayableValue.of(value.balance)
            return UiCashCard(
                id = value.id,
                label = UiText.DynamicString(value.name),
                title = UiText.StringResourceId(Res.string.total_of_ore, balance.value),
                iconBackground = CardColor.from(value.color),
                icon = CardIcon.from(value.icon),
                name = value.name,
                balance = balance
            )
        }
    }
}
