package com.wynndie.spwallet.sharedFeature.wallet.data.remote.model

import com.wynndie.spwallet.sharedFeature.wallet.domain.model.card.CardBalance
import kotlinx.serialization.Serializable

@Serializable
data class CardBalanceDto(
    val balance: Long
) {
    fun toCardBalance(): CardBalance = CardBalance(balance)
}
