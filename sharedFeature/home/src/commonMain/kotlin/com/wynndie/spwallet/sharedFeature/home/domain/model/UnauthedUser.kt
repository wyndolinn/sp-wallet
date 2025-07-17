package com.wynndie.spwallet.sharedFeature.home.domain.model

import com.wynndie.spwallet.sharedFeature.home.domain.model.card.UnauthedCard

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
