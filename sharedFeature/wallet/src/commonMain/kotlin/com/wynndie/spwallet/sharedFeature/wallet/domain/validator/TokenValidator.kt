package com.wynndie.spwallet.sharedFeature.wallet.domain.validator

import com.wynndie.spwallet.sharedCore.domain.error.Outcome
import com.wynndie.spwallet.sharedCore.domain.error.ValidationError
import com.wynndie.spwallet.sharedCore.domain.validator.Validator

class TokenValidator : Validator<String> {

    override fun validate(value: String): Outcome<Boolean, ValidationError> {
        if (value.isBlank())
            return Outcome.Error(ValidationError.EMPTY_FIELD)

        return Outcome.Success(true)
    }
}