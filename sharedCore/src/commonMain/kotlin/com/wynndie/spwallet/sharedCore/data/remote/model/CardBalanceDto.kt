package com.wynndie.spwallet.sharedCore.data.remote.model

import com.wynndie.spwallet.sharedCore.domain.model.CardBalance
import kotlinx.serialization.Serializable

@Serializable
data class CardBalanceDto(
    val balance: Long
) {
    fun toCardBalance(): CardBalance = CardBalance(balance)
}