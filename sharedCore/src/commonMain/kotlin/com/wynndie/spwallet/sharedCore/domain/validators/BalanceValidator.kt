package com.wynndie.spwallet.sharedCore.domain.validators

import com.wynndie.spwallet.sharedCore.domain.error.ValidationError

class BalanceValidator : Validator<String> {

    override fun validate(value: String): Pair<Boolean, ValidationError?> {
        if (value.isBlank())
            return false to ValidationError.EMPTY_FIELD

        if (!value.matches(Regex("^[0-9]+$")))
            return false to ValidationError.INVALID_CHARACTERS

        if (value.toLong() <= 0)
            return false to ValidationError.INSUFFICIENT_VALUE

        return true to null
    }
}