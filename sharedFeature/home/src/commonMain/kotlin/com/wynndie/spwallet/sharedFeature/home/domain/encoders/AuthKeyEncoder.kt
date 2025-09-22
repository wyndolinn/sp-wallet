package com.wynndie.spwallet.sharedFeature.home.domain.encoders

interface AuthKeyEncoder {
    fun encode(id: String, token: String): String
}