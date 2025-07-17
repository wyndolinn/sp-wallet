package com.wynndie.spwallet.sharedFeature.home.domain.validator

import com.wynndie.spwallet.sharedCore.domain.error.ValidationError
import com.wynndie.spwallet.sharedCore.domain.validator.Validator

class CardNumberValidator : Validator<String> {

    override fun validate(value: String): Pair<Boolean, ValidationError?> {
        if (value.isBlank())
            return false to ValidationError.EMPTY_FIELD

        if (!value.matches(Regex("^[0-9]+$")))
            return false to ValidationError.INVALID_CHARACTERS

        if (value.length != 5)
            return false to ValidationError.EXACT_VALUE_REQUIRED

        return true to null
    }
}