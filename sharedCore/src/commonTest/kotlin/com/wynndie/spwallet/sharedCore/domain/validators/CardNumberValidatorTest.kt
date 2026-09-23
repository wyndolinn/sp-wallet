package com.wynndie.spwallet.sharedCore.domain.validators

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.wynndie.spwallet.sharedCore.domain.outcome.Error
import com.wynndie.spwallet.sharedCore.domain.validators.core.ValidationValues
import kotlin.test.Test

class CardNumberValidatorTest {

    private val validator = CardNumberValidator()

    @Test
    fun `validate should return error when card number is empty`() {
        val values = ValidationValues(value = "")
        val result = validator.validate(values)
        assertThat(result).isEqualTo(false to Error.Validation.EMPTY_FIELD)
    }

    @Test
    fun `validate should return error when card number contains non-digits`() {
        val values = ValidationValues(value = "1234a")
        val result = validator.validate(values)
        assertThat(result).isEqualTo(false to Error.Validation.INVALID_CHARACTERS)
    }
}
