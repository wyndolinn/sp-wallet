package com.wynndie.spwallet.sharedCore.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CardBalanceDto(
    val balance: Long
)