package com.wynndie.spwallet.sharedCore.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CustomCardEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val server: String,
    val name: String,
    val balance: Long,
    val color: Int,
    val icon: Int
)
