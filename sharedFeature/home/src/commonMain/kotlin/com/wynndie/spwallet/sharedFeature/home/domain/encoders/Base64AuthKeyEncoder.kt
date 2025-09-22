package com.wynndie.spwallet.sharedFeature.home.domain.encoders

import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class Base64AuthKeyEncoder : AuthKeyEncoder {

    @OptIn(ExperimentalEncodingApi::class)
    override fun encode(id: String, token: String): String {
        val encodedKey = Base64.encode("$id:$token".encodeToByteArray())
        return "Bearer $encodedKey"
    }
}