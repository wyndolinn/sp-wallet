package com.wynndie.spwallet.sharedFeature.home.domain.validator

import com.wynndie.spwallet.sharedCore.domain.error.ValidationError
import com.wynndie.spwallet.sharedCore.domain.validators.Validator

class TokenValidator : Validator<String> {

    override fun validate(value: String): Pair<Boolean, ValidationError?> {
        if (value.isBlank())
            return false to ValidationError.EMPTY_FIELD

        return true to null
    }
}