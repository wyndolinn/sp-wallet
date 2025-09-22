package com.wynndie.spwallet.sharedCore.domain.models

data class UnauthedUser(
    val id: String,
    val name: String,
    val cards: List<UnauthedCard>
) {
    fun toAuthedUser(): AuthedUser {
        return AuthedUser(
            id = id,
            name = name
        )
    }
}