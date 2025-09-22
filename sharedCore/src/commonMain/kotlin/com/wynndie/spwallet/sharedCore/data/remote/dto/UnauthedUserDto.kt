package com.wynndie.spwallet.sharedCore.data.remote.dto

import com.wynndie.spwallet.sharedCore.domain.models.UnauthedUser
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UnauthedUserDto(
    val id: String,
    @SerialName(value = "username")
    val name: String,
    val cards: List<UnauthedCardDto>,
) {
    fun toUnauthedUser(): UnauthedUser {
        return UnauthedUser(
            id = id,
            name = name,
            cards = cards.map { it.toUnauthedCard() }
        )
    }
}