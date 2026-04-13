package com.wynndie.spwallet.sharedCore.domain.validators

import com.wynndie.spwallet.sharedCore.domain.outcome.Error

class CardNameValidator : Validator<String> {

    override fun validate(value: String): Pair<Boolean, Error.Validation?> {
        if (value.isBlank())
            return false to Error.Validation.EMPTY_FIELD

        return true to null
    }
}