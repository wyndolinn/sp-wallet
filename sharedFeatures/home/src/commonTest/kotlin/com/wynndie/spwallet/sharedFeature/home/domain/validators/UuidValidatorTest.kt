package com.wynndie.spwallet.sharedFeature.home.domain.validators

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.wynndie.spwallet.sharedCore.domain.outcome.Error
import com.wynndie.spwallet.sharedCore.domain.validators.core.ValidationValues
import kotlin.test.Test

class UuidValidatorTest {

    private val validator = UuidValidator()

    @Test
    fun `validate should return valid when uuid is correct`() {
        val values = ValidationValues(value = "123e4567-e89b-12d3-a456-426614174000")
        val result = validator.validate(values)
        assertThat(result).isEqualTo(true to null)
    }

    @Test
    fun `validate should return error when uuid is empty`() {
        val values = ValidationValues(value = "")
        val result = validator.validate(values)
        assertThat(result).isEqualTo(false to Error.Validation.EMPTY_FIELD)
    }

    @Test
    fun `validate should return error when uuid contains invalid characters`() {
        val values = ValidationValues(value = "123e4567-e89b-12d3-a456-42661417400z")
        val result = validator.validate(values)
        assertThat(result).isEqualTo(false to Error.Validation.INVALID_CHARACTERS)
    }

    @Test
    fun `validate should return error when uuid format is incorrect`() {
        val values = ValidationValues(value = "123e4567-e89b-12d3-a456")
        val result = validator.validate(values)
        assertThat(result).isEqualTo(false to Error.Validation.INVALID_FORMAT)
    }
}
