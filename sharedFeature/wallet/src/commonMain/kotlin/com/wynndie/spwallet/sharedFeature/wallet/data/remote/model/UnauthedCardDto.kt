package com.wynndie.spwallet.sharedFeature.wallet.data.remote.model

import com.wynndie.spwallet.sharedFeature.wallet.domain.model.card.UnauthedCard
import kotlinx.serialization.Serializable

@Serializable
data class UnauthedCardDto(
    val id: String,
    val name: String,
    val number: String,
    val color: Int,
) {
    fun toUnauthedCard(): UnauthedCard {
        return UnauthedCard(
            id = id,
            name = name,
            color = color,
            icon = 2,
            number = number
        )
    }
}
