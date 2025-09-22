package com.wynndie.spwallet.sharedCore.data.remote.dto

import com.wynndie.spwallet.sharedCore.domain.models.CardBalance
import kotlinx.serialization.Serializable

@Serializable
data class CardBalanceDto(
    val balance: Long
) {
    fun toCardBalance(): CardBalance = CardBalance(balance)
}