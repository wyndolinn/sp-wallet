package com.wynndie.spwallet.sharedCore.domain.models

import com.wynndie.spwallet.sharedCore.domain.models.cards.UnauthedCard

data class UnauthedUser(
    val id: String,
    val server: SpServersOptions,
    val username: String,
    val cards: List<UnauthedCard>
) {
    fun toAuthedUser(): AuthedUser {
        return AuthedUser(
            id = id,
            name = username
        )
    }
}