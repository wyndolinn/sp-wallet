package com.wynndie.spwallet.sharedCore.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecipientEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val server: String,
    val name: String,
    val number: String,
    val color: Int,
    val icon: Int
)
