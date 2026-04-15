package com.wynndie.spwallet.sharedCore.domain.validators

import com.wynndie.spwallet.sharedCore.domain.outcome.Error

class CardNumberValidator : Validator<String> {

    override fun validate(value: String): Pair<Boolean, Error.Validation?> {
        if (value.isBlank())
            return false to Error.Validation.EMPTY_FIELD

        if (!value.matches(Regex("^[0-9]+$")))
            return false to Error.Validation.INVALID_CHARACTERS

        if (value.length != 5)
            return false to Error.Validation.EXACT_VALUE_REQUIRED

        return true to null
    }
}