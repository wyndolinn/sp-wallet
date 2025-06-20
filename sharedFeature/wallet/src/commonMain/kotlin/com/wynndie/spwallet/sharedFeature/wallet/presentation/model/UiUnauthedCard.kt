package com.wynndie.spwallet.sharedFeature.wallet.presentation.model

import com.wynndie.spwallet.sharedCore.presentation.model.UiText
import com.wynndie.spwallet.sharedFeature.wallet.domain.model.card.UnauthedCard
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.bank_card_label
import com.wynndie.spwallet.sharedResources.require_activation

data class UiUnauthedCard(
    override val id: String,
    override val icon: CardIcon,
    override val iconBackground: CardColor,
    override val label: UiText,
    override val title: UiText,
    override val description: UiText? = null,
    val name: String,
    val number: String,
) : UiCard {

    fun toDomain(): UnauthedCard {
        return UnauthedCard(
            id = id,
            name = name,
            color = iconBackground.ordinal,
            icon = icon.ordinal,
            number = number
        )
    }

    companion object {
        fun of(value: UnauthedCard): UiUnauthedCard {
            return UiUnauthedCard(
                id = value.id,
                label = UiText.StringResourceId(
                    Res.string.bank_card_label,
                    value.name,
                    value.number
                ),
                title = UiText.StringResourceId(Res.string.require_activation),
                iconBackground = CardColor.from(value.color),
                icon = CardIcon.from(value.icon),
                name = value.name,
                number = value.number
            )
        }
    }
}
