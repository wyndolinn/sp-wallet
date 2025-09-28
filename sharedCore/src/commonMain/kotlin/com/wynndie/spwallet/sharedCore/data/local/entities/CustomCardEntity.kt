package com.wynndie.spwallet.sharedCore.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardColors
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardIcons
import com.wynndie.spwallet.sharedCore.domain.models.cards.CustomCard

@Entity
data class CustomCardEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val balance: Long,
    val color: Int,
    val icon: Int
) {
    fun toDomain(): CustomCard {
        return CustomCard(
            id = id.toString(),
            name = name,
            balance = balance,
            color = CardColors.of(color),
            icon = CardIcons.of(icon)
        )
    }

    companion object {
        fun of(value: CustomCard): CustomCardEntity {
            return CustomCardEntity(
                id = if (value.id.isBlank()) 0 else value.id.toInt(),
                name = value.name,
                balance = value.balance,
                color = value.color.id,
                icon = value.icon.id
            )
        }
    }
}
