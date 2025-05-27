package com.wynndie.spwallet.sharedFeature.wallet.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wynndie.spwallet.sharedFeature.wallet.domain.model.card.RecipientCard

@Entity
data class RecipientEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val color: Int,
    val icon: Int,
    val number: String
) {
    fun toRecipient(): RecipientCard {
        return RecipientCard(
            id = id.toString(),
            name = name,
            color = color,
            icon = icon,
            number = number
        )
    }

    companion object {
        fun from(recipientCard: RecipientCard): RecipientEntity {
            return RecipientEntity(
                id = if (recipientCard.id == "") 0 else recipientCard.id.toInt(),
                name = recipientCard.name,
                color = recipientCard.color,
                icon = recipientCard.icon,
                number = recipientCard.number
            )
        }
    }
}
