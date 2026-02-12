package com.wynndie.spwallet.sharedCore.domain.models

data class AuthedUser(
    val id: String,
    val server: SpServers,
    val name: String
)