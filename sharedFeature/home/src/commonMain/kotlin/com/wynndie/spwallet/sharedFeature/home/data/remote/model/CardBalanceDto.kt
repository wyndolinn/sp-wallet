package com.wynndie.spwallet.sharedFeature.home.data.remote.model

import com.wynndie.spwallet.sharedFeature.home.domain.model.card.CardBalance
import kotlinx.serialization.Serializable

@Serializable
data class CardBalanceDto(
    val balance: Long
) {
    fun toCardBalance(): CardBalance = CardBalance(balance)
}
