package com.wynndie.spwallet.sharedCore.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class UnauthedCardDto(
    val id: String,
    val name: String,
    val number: String,
    val color: Int
)