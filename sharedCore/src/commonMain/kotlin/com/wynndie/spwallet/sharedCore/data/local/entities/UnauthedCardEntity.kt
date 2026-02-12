package com.wynndie.spwallet.sharedCore.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wynndie.spwallet.sharedCore.domain.models.SpServers
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardColors
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardIcons
import com.wynndie.spwallet.sharedCore.domain.models.cards.UnauthedCard

@Entity
data class UnauthedCardEntity(
    @PrimaryKey
    val id: String,
    val server: String,
    val name: String,
    val number: String,
    val color: Int,
    val icon: Int
) {
    fun toDomain(): UnauthedCard {
        return UnauthedCard(
            id = id,
            server = SpServers.valueOf(server),
            name = name,
            number = number,
            color = CardColors.of(color),
            icon = CardIcons.of(icon)
        )
    }

    companion object {
        fun from(value: UnauthedCard): UnauthedCardEntity {
            return UnauthedCardEntity(
                id = value.id,
                server = value.server.name,
                name = value.name,
                number = value.number,
                color = value.color.id,
                icon = value.icon.id
            )
        }
    }
}
