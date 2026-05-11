package com.wynndie.spwallet.sharedCore.data.mappers

import com.wynndie.spwallet.sharedCore.data.network.dto.CardholderDto
import com.wynndie.spwallet.sharedCore.data.network.dto.UnauthedCardDto
import com.wynndie.spwallet.sharedCore.domain.models.Cardholder
import com.wynndie.spwallet.sharedCore.domain.models.SpServers
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardColors
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardIcons
import com.wynndie.spwallet.sharedCore.domain.models.cards.UnauthedCard

fun CardholderDto.toDomain(server: SpServers): Cardholder {
    return Cardholder(
        id = id,
        server = server,
        username = username,
        cards = cards.map { it.toDomain(server) }
    )
}

fun UnauthedCardDto.toDomain(server: SpServers): UnauthedCard {
    return UnauthedCard(
        id = id,
        server = server,
        name = name,
        number = number,
        color = CardColors.of(color),
        icon = CardIcons.ADD_CARD
    )
}