package com.wynndie.spwallet.sharedFeature.transfer.domain.models

import com.wynndie.spwallet.sharedCore.domain.models.SpServers
import com.wynndie.spwallet.sharedCore.domain.models.cards.AuthedCard
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardColors
import com.wynndie.spwallet.sharedCore.domain.models.cards.CardIcons
import com.wynndie.spwallet.sharedCore.domain.models.cards.UnauthedCard

data class TransferCard(
    val id: String,
    val server: SpServers,
    val name: String,
    val number: String,
    val balance: Long?,
    val color: CardColors,
    val icon: CardIcons
) {
    companion object {
        fun of(card: AuthedCard): TransferCard {
            return TransferCard(
                id = card.id,
                server = card.server,
                name = card.name,
                number = card.number,
                balance = card.balance,
                color = card.color,
                icon = card.icon
            )
        }

        fun of(card: UnauthedCard): TransferCard {
            return TransferCard(
                id = card.id,
                server = card.server,
                name = card.name,
                number = card.number,
                balance = null,
                color = card.color,
                icon = card.icon
            )
        }
    }
}
