package com.wynndie.spwallet.sharedCore.data.remote.dto

import com.wynndie.spwallet.sharedCore.domain.models.SpServers
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardColors
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardIcons
import com.wynndie.spwallet.sharedCore.domain.models.cards.UnauthedCard
import kotlinx.serialization.Serializable

@Serializable
data class UnauthedCardDto(
    val id: String,
    val name: String,
    val number: String,
    val color: Int
) {
    fun toDomain(server: SpServers): UnauthedCard {
        return UnauthedCard(
            id = id,
            authKey = "",
            server = server,
            name = name,
            number = number,
            balance = 0L,
            color = CardColors.of(color),
            icon = CardIcons.ADD_CARD
        )
    }
}