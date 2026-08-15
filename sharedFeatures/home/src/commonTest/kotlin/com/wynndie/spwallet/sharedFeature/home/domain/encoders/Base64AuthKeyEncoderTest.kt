package com.wynndie.spwallet.sharedFeature.home.domain.encoders

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEqualTo
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.test.Test

class Base64AuthKeyEncoderTest {

    private val encoder = Base64AuthKeyEncoder()

    @OptIn(ExperimentalEncodingApi::class)
    @Test
    fun `encode should return Bearer token with base64 encoded id and token`() {
        val id = "cardId"
        val token = "cardToken"

        val result = encoder.encode(id, token)

        val expectedEncoded = Base64.encode("$id:$token".encodeToByteArray())
        assertThat(result).isEqualTo("Bearer $expectedEncoded")
    }

    @Test
    fun `encode should produce different results for different inputs`() {
        val result1 = encoder.encode("id1", "token1")
        val result2 = encoder.encode("id2", "token2")

        assertThat(result1).isNotEqualTo(result2)
    }

    @Test
    fun `encode should be deterministic for same input`() {
        val result1 = encoder.encode("id", "token")
        val result2 = encoder.encode("id", "token")

        assertThat(result1).isEqualTo(result2)
    }
}