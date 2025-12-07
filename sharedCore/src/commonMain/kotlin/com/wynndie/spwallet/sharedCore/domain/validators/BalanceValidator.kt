package com.wynndie.spwallet.sharedCore.domain.validators

import com.wynndie.spwallet.sharedCore.domain.error.ValidationError

class BalanceValidator(
    private val maxValue: Int = 999_999_999,
    private val minValue: Int = 0
) : Validator<String> {

    override fun validate(value: String): Pair<Boolean, ValidationError?> {
        if (value.isBlank())
            return false to ValidationError.EMPTY_FIELD

        if (!value.matches(Regex("^[0-9]+$")))
            return false to ValidationError.INVALID_CHARACTERS

        if (value.toLong() < minValue)
            return false to ValidationError.BELOW_MINIMUM_VALUE

        if (value.toLong() > maxValue)
            return false to ValidationError.ABOVE_MAXIMUM_VALUE

        return true to null
    }
}