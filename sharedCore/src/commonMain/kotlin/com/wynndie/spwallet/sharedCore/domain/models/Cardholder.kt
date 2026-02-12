package com.wynndie.spwallet.sharedCore.domain.models

import com.wynndie.spwallet.sharedCore.domain.models.cards.UnauthedCard

data class Cardholder(
    val id: String,
    val server: SpServers,
    val username: String,
    val cards: List<UnauthedCard>
) {
    fun toAuthedUser(): AuthedUser {
        return AuthedUser(
            id = id,
            server = server,
            name = username
        )
    }
}