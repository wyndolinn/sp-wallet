package com.wynndie.spwallet.sharedCore.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wynndie.spwallet.sharedCore.domain.models.SpServers
import com.wynndie.spwallet.sharedCore.domain.models.cards.AuthedCard
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardColors
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardIcons

@Entity
data class AuthedCardEntity(
    @PrimaryKey
    val id: String,
    val authKey: String,
    val server: String,
    val name: String,
    val number: String,
    val balance: Long,
    val color: Int,
    val icon: Int
) {
    fun toDomain(): AuthedCard {
        return AuthedCard(
            id = id,
            authKey = authKey,
            server = SpServers.valueOf(server),
            name = name,
            number = number,
            balance = balance,
            color = CardColors.of(color),
            icon = CardIcons.of(icon)
        )
    }

    companion object {
        fun of(value: AuthedCard): AuthedCardEntity {
            return AuthedCardEntity(
                id = value.id,
                authKey = value.authKey,
                server = value.server.name,
                name = value.name,
                number = value.number,
                balance = value.balance,
                color = value.color.id,
                icon = value.icon.id
            )
        }
    }
}
