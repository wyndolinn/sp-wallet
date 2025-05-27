package com.wynndie.spwallet.sharedFeature.wallet.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wynndie.spwallet.sharedFeature.wallet.domain.model.card.AuthedCard

@Entity
data class AuthedCardEntity(
    @PrimaryKey val id: String,
    val name: String,
    val color: Int,
    val number: String,
    val authKey: String,
    val balance: Long
) {
    fun toAuthedCard(): AuthedCard {
        return AuthedCard(
            id = id,
            name = name,
            color = color,
            icon = 1,
            number = number,
            authKey = authKey,
            balance = balance
        )
    }

    companion object {
        fun from(card: AuthedCard): AuthedCardEntity {
            return AuthedCardEntity(
                id = card.id,
                name = card.name,
                color = card.color,
                authKey = card.authKey,
                balance = card.balance,
                number = card.number
            )
        }
    }
}
