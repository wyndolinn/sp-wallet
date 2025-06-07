package com.wynndie.spwallet.sharedFeature.wallet.presentation.model

import androidx.compose.runtime.Composable
import com.wynndie.spwallet.sharedCore.presentation.mapper.joinAsString
import com.wynndie.spwallet.sharedCore.presentation.model.UiText
import com.wynndie.spwallet.sharedFeature.wallet.domain.model.card.AuthedCard
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.bank_card_label
import com.wynndie.spwallet.sharedResources.total_of_ore

data class UiAuthedCard(
    override val id: String,
    override val icon: CardIcon,
    override val iconBackground: CardColor,
    override val label: UiText,
    override val title: UiText,
    override val description: UiText? = null,
    val authKey: String,
    val name: String,
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
    fun formatDescription(): UiAuthedCard {
        return this.copy(
            description = UiText.DynamicString(
                balance.formatted.joinAsString(" ")
            )
        )
    }

    companion object {
        fun of(value: AuthedCard): UiAuthedCard {
            val balance = BlocksDisplayableValue.of(value.balance)
            return UiAuthedCard(
                id = value.id,
                label = UiText.StringResourceId(
                    Res.string.bank_card_label,
                    value.name,
                    value.number
                ),
                title = UiText.StringResourceId(Res.string.total_of_ore, balance.value),
                iconBackground = CardColor.from(value.color),
                icon = CardIcon.from(value.icon),
                name = value.name,
                number = value.number,
                authKey = value.authKey,
                balance = balance
            )
        }
    }
}
