package com.wynndie.spwallet.sharedCore.data.remote.dto

import com.wynndie.spwallet.sharedCore.domain.models.UnauthedUser
import kotlinx.serialization.Serializable

@Serializable
data class UnauthedUserDto(
    val id: String,
    val username: String,
    val cards: List<UnauthedCardDto>,
) {
    fun toUnauthedUser(): UnauthedUser {
        return UnauthedUser(
            id = id,
            username = username,
            cards = cards.map { it.toUnauthedCard() }
        )
    }
}