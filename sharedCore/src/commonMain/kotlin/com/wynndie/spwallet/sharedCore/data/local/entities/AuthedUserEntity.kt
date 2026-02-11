package com.wynndie.spwallet.sharedCore.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wynndie.spwallet.sharedCore.domain.models.AuthedUser
import com.wynndie.spwallet.sharedCore.domain.models.SpServersOptions

@Entity
data class AuthedUserEntity(
    @PrimaryKey val id: String,
    val name: String
) {
    fun toDomain(): AuthedUser {
        return AuthedUser(
            id = id,
            name = name
        )
    }

    companion object {
        fun from(user: AuthedUser): AuthedUserEntity {
            return AuthedUserEntity(
                id = user.id,
                name = user.name
            )
        }
    }
}
