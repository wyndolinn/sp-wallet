package com.wynndie.spwallet.sharedCore.domain.validators.core

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.wynndie.spwallet.sharedCore.domain.outcome.Error
import kotlin.test.Test

class ValidationChainTest {

    @Test
    fun `ensureNotEmpty should return error when value is blank`() {
        val result = ValidationChain("").ensureNotEmpty().build()
        assertThat(result).isEqualTo(false to Error.Validation.EMPTY_FIELD)
    }

    @Test
    fun `ensureValidCharacters should return error when regex does not match`() {
        val result = ValidationChain("abc").ensureValidCharacters(Regex("^[0-9]+$")).build()
        assertThat(result).isEqualTo(false to Error.Validation.INVALID_CHARACTERS)
    }

    @Test
    fun `ensureValidFormat should return error when regex does not match`() {
        val result = ValidationChain("abc").ensureValidFormat(Regex("^[0-9]+$")).build()
        assertThat(result).isEqualTo(false to Error.Validation.INVALID_FORMAT)
    }

    @Test
    fun `ensureValueAtMost should return error when value is greater or equal`() {
        val result = ValidationChain("100").ensureValueAtMost(100).build()
        assertThat(result).isEqualTo(false to Error.Validation.BELOW_MINIMUM_VALUE)
    }

    @Test
    fun `ensureValueAtLeast should return error when value is less or equal`() {
        val result = ValidationChain("10").ensureValueAtLeast(10).build()
        assertThat(result).isEqualTo(false to Error.Validation.ABOVE_MAXIMUM_VALUE)
    }

    @Test
    fun `ensureExactValue should return error when value is different`() {
        val result = ValidationChain("10").ensureExactValue(20).build()
        assertThat(result).isEqualTo(false to Error.Validation.EXACT_VALUE_REQUIRED)
    }

    @Test
    fun `ensureExactLength should return error when length is different`() {
        val result = ValidationChain("123").ensureExactLength(5).build()
        assertThat(result).isEqualTo(false to Error.Validation.EXACT_LENGTH_REQUIRED)
    }

    @Test
    fun `chain should stop on first error`() {
        val result = ValidationChain("")
            .ensureNotEmpty()
            .ensureValidCharacters(Regex("^[0-9]+$"))
            .build()

        assertThat(result).isEqualTo(false to Error.Validation.EMPTY_FIELD)
    }

    @Test
    fun `chain should return valid when all rules pass`() {
        val result = ValidationChain("123")
            .ensureNotEmpty()
            .ensureValidCharacters(Regex("^[0-9]+$"))
            .build()

        assertThat(result).isEqualTo(true to null)
    }
}
