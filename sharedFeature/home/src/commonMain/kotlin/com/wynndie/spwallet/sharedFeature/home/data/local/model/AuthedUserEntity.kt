package com.wynndie.spwallet.sharedFeature.home.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wynndie.spwallet.sharedFeature.home.domain.model.AuthedUser

@Entity
data class AuthedUserEntity(
    @PrimaryKey val id: String,
    val name: String
) {
    fun toAuthedUser(): AuthedUser {
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
