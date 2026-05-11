package com.wynndie.spwallet.sharedCore.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AuthedUserEntity(
    @PrimaryKey val id: String,
    val server: String,
    val name: String
)
