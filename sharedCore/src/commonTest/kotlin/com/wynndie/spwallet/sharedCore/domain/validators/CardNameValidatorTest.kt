package com.wynndie.spwallet.sharedCore.domain.validators

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.wynndie.spwallet.sharedCore.domain.outcome.Error
import com.wynndie.spwallet.sharedCore.domain.validators.core.ValidationValues
import kotlin.test.Test

class CardNameValidatorTest {

    private val validator = CardNameValidator()

    @Test
    fun `validate should return valid when name is not empty`() {
        val values = ValidationValues(value = "My Card")
        val result = validator.validate(values)
        assertThat(result).isEqualTo(true to null)
    }

    @Test
    fun `validate should return error when name is empty`() {
        val values = ValidationValues(value = "   ")
        val result = validator.validate(values)
        assertThat(result).isEqualTo(false to Error.Validation.EMPTY_FIELD)
    }
}
