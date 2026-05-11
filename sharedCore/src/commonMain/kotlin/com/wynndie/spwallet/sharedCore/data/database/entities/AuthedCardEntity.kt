package com.wynndie.spwallet.sharedCore.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

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
)
