package com.wynndie.spwallet.sharedCore.data.remote.dto

import com.wynndie.spwallet.sharedCore.domain.models.SpServersOptions
import com.wynndie.spwallet.sharedCore.domain.models.UnauthedUser
import kotlinx.serialization.Serializable

@Serializable
data class UnauthedUserDto(
    val id: String,
    val username: String,
    val cards: List<UnauthedCardDto>,
) {
    fun toUnauthedUser(server: SpServersOptions): UnauthedUser {
        return UnauthedUser(
            id = id,
            server = server,
            username = username,
            cards = cards.map { it.toUnauthedCard(server) }
        )
    }
}