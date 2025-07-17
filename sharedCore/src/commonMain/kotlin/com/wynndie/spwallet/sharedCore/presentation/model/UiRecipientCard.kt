package com.wynndie.spwallet.sharedCore.presentation.model

import androidx.compose.runtime.Composable
import com.wynndie.spwallet.sharedCore.domain.model.RecipientCard
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.enter_card_number
import com.wynndie.spwallet.sharedResources.recipient

data class UiRecipientCard(
    override val id: String,
    override val icon: CardIcon,
    override val iconBackground: CardColor,
    override val label: UiText,
    override val title: UiText,
    override val description: UiText? = null,
    val name: String,
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
    fun formatTitle(): UiRecipientCard {
        return this.copy(
            title = if (number.isBlank()) {
                UiText.StringResourceId(Res.string.enter_card_number)
            } else {
                title
            }
        )
    }

    @Composable
    fun formatDescription(): UiRecipientCard {
        return this.copy(
            description = if (name.isBlank()) {
                null
            } else {
                description
            }
        )
    }

    companion object {
        fun of(value: RecipientCard): UiRecipientCard {
            return UiRecipientCard(
                id = value.id,
                label = UiText.StringResourceId(Res.string.recipient),
                title = UiText.DynamicString(value.number),
                description = UiText.DynamicString(value.name),
                iconBackground = CardColor.from(value.color),
                icon = CardIcon.from(value.icon),
                name = value.name,
                number = value.number
            )
        }
    }
}