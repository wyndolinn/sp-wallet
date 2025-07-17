package com.wynndie.spwallet.sharedCore.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wynndie.spwallet.sharedCore.domain.model.CashCard

@Entity
data class CashCardEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val color: Int,
    val balance: Long
) {
    fun toCashCard(): CashCard {
        return CashCard(
            id = id.toString(),
            name = name,
            color = color,
            icon = 0,
            balance = balance
        )
    }

    companion object {
        fun from(card: CashCard): CashCardEntity {
            return CashCardEntity(
                id = if (card.id == "") 0 else card.id.toInt(),
                name = card.name,
                color = card.color,
                balance = card.balance
            )
        }
    }
}
