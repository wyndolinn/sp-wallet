package com.wynndie.spwallet.sharedCore.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wynndie.spwallet.sharedCore.domain.model.UnauthedCard

@Entity
data class UnauthedCardEntity(
    @PrimaryKey val id: String,
    val name: String,
    val color: Int,
    val number: String,
) {
    fun toUnauthedCard(): UnauthedCard {
        return UnauthedCard(
            id = id,
            name = name,
            color = color,
            icon = 2,
            number = number,
        )
    }

    companion object {
        fun from(card: UnauthedCard): UnauthedCardEntity {
            return UnauthedCardEntity(
                id = card.id,
                name = card.name,
                number = card.number,
                color = card.color
            )
        }
    }
}
