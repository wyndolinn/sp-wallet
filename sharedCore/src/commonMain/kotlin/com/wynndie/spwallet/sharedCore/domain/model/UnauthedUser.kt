package com.wynndie.spwallet.sharedCore.domain.model

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