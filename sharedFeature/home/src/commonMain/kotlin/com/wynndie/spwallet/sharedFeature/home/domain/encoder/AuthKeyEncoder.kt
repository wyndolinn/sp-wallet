package com.wynndie.spwallet.sharedFeature.home.domain.encoder

interface AuthKeyEncoder {
    fun encode(id: String, token: String): String
}