package com.wynndie.spwallet.sharedCore.domain.models

data class AuthedUser(
    val id: String,
    val server: Servers,
    val name: String,
)