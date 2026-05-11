package com.wynndie.spwallet.sharedCore.data.network.dto

import com.wynndie.spwallet.sharedCore.domain.models.SpServers
import com.wynndie.spwallet.sharedCore.domain.models.Cardholder
import kotlinx.serialization.Serializable

@Serializable
data class CardholderDto(
    val id: String,
    val username: String,
    val cards: List<UnauthedCardDto>,
) {
    fun toDomain(server: SpServers): Cardholder {
        return Cardholder(
            id = id,
            server = server,
            username = username,
            cards = cards.map { it.toDomain(server) }
        )
    }
}