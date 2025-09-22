package com.wynndie.spwallet.sharedCore.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wynndie.spwallet.sharedCore.domain.models.CustomCard

@Entity
data class CustomCardEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val color: Int,
    val balance: Long
) {
    fun toDomain(): CustomCard {
        return CustomCard(
            id = id.toString(),
            name = name,
            color = color,
            icon = 0,
            balance = balance
        )
    }

    companion object {
        fun from(card: CustomCard): CustomCardEntity {
            return CustomCardEntity(
                id = if (card.id == "") 0 else card.id.toInt(),
                name = card.name,
                color = card.color,
                balance = card.balance
            )
        }
    }
}
