package com.wynndie.spwallet.sharedCore.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class CardBalanceDto(
    val balance: Long
)