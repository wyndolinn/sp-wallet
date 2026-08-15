package com.wynndie.spwallet.sharedFeature.transfer.domain.validators

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.wynndie.spwallet.sharedCore.domain.outcome.Error
import com.wynndie.spwallet.sharedCore.domain.validators.core.ValidationValues
import kotlin.test.Test

class TransferCommentValidatorTest {

    private val validator = TransferCommentValidator()

    @Test
    fun `validate should return valid when comment is not empty`() {
        val values = ValidationValues(value = "Some comment")
        val result = validator.validate(values)
        assertThat(result).isEqualTo(true to null)
    }

    @Test
    fun `validate should return error when comment is empty`() {
        val values = ValidationValues(value = "   ")
        val result = validator.validate(values)
        assertThat(result).isEqualTo(false to Error.Validation.EMPTY_FIELD)
    }
}
