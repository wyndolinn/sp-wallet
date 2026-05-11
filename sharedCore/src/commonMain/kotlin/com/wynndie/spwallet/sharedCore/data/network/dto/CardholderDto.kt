package com.wynndie.spwallet.sharedCore.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class CardholderDto(
    val id: String,
    val username: String,
    val cards: List<UnauthedCardDto>,
)