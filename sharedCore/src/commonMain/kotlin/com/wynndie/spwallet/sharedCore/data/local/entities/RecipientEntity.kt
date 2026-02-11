package com.wynndie.spwallet.sharedCore.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wynndie.spwallet.sharedCore.domain.models.SpServersOptions
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardColors
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardIcons
import com.wynndie.spwallet.sharedCore.domain.models.cards.RecipientCard

@Entity
data class RecipientEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val server: String,
    val name: String,
    val number: String,
    val color: Int,
    val icon: Int
) {
    fun toDomain(): RecipientCard {
        return RecipientCard(
            id = id.toString(),
            server = SpServersOptions.valueOf(server),
            name = name,
            number = number,
            color = CardColors.of(color),
            icon = CardIcons.of(icon)
        )
    }

    companion object {
        fun of(value: RecipientCard): RecipientEntity {
            return RecipientEntity(
                id = if (value.id.isBlank()) 0 else value.id.toInt(),
                server = value.server.name,
                name = value.name,
                number = value.number,
                color = value.color.id,
                icon = value.icon.id
            )
        }
    }
}
