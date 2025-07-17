package com.wynndie.spwallet.sharedCore.domain.validator

import com.wynndie.spwallet.sharedCore.domain.error.ValidationError

class CardNameValidator : Validator<String> {

    override fun validate(value: String): Pair<Boolean, ValidationError?> {
        if (value.isBlank())
            return false to ValidationError.EMPTY_FIELD

        return true to null
    }
}