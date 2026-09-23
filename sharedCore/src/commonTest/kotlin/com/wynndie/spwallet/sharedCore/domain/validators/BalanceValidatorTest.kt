package com.wynndie.spwallet.sharedCore.domain.validators

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.wynndie.spwallet.sharedCore.domain.outcome.Error
import com.wynndie.spwallet.sharedCore.domain.validators.core.ValidationValues
import kotlin.test.Test

class BalanceValidatorTest {

    private val validator = BalanceValidator()

    @Test
    fun `validate should return valid when balance is correct within range`() {
        val values = ValidationValues(value = "500", minValue = 0, maxValue = 1000)
        val result = validator.validate(values)
        assertThat(result).isEqualTo(true to null)
    }

    @Test
    fun `validate should return error when balance is empty`() {
        val values = ValidationValues(value = "", minValue = 0, maxValue = 1000)
        val result = validator.validate(values)
        assertThat(result).isEqualTo(false to Error.Validation.EMPTY_FIELD)
    }

    @Test
    fun `validate should return error when balance contains non-digits`() {
        val values = ValidationValues(value = "123a", minValue = 0, maxValue = 1000)
        val result = validator.validate(values)
        assertThat(result).isEqualTo(false to Error.Validation.INVALID_CHARACTERS)
    }

    @Test
    fun `validate should return error when balance exceeds max value`() {
        val values = ValidationValues(value = "1500", minValue = 0, maxValue = 1000)
        val result = validator.validate(values)
        assertThat(result).isEqualTo(false to Error.Validation.BELOW_MINIMUM_VALUE)
    }

    @Test
    fun `validate should return error when balance is below min value`() {
        val values = ValidationValues(value = "5", minValue = 10, maxValue = 1000)
        val result = validator.validate(values)
        assertThat(result).isEqualTo(false to Error.Validation.ABOVE_MAXIMUM_VALUE)
    }
}
