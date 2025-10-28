package com.wynndie.spwallet.sharedCore.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class AuthedUser(
    val id: String,
    val name: String
)