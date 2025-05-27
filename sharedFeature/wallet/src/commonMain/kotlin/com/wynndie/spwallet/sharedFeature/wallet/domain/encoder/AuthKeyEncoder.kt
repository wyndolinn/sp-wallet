package com.wynndie.spwallet.sharedFeature.wallet.domain.encoder

interface AuthKeyEncoder {
    fun encode(id: String, token: String): String
}