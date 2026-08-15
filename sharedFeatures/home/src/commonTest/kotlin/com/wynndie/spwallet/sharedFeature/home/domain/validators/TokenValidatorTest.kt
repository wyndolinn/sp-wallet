package com.wynndie.spwallet.sharedFeature.home.domain.validators

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.wynndie.spwallet.sharedCore.domain.outcome.Error
import com.wynndie.spwallet.sharedCore.domain.validators.core.ValidationValues
import kotlin.test.Test

class TokenValidatorTest {

    private val validator = TokenValidator()

    @Test
    fun `validate should return valid when token is correct base64`() {
        val values = ValidationValues(value = "SGVsbG8=")
        val result = validator.validate(values)
        assertThat(result).isEqualTo(true to null)
    }

    @Test
    fun `validate should return error when token is empty`() {
        val values = ValidationValues(value = "")
        val result = validator.validate(values)
        assertThat(result).isEqualTo(false to Error.Validation.EMPTY_FIELD)
    }

    @Test
    fun `validate should return error when token contains invalid base64 characters`() {
        val values = ValidationValues(value = "SGVsbG8=_")
        val result = validator.validate(values)
        assertThat(result).isEqualTo(false to Error.Validation.INVALID_CHARACTERS)
    }

    @Test
    fun `validate should return error when token has invalid base64 padding`() {
        val values = ValidationValues(value = "SGVsbG8")
        val result = validator.validate(values)
        assertThat(result).isEqualTo(false to Error.Validation.INVALID_FORMAT)
    }
}
