package com.wynndie.spwallet.sharedFeature.wallet.domain.validator

import com.wynndie.spwallet.sharedCore.domain.error.Outcome
import com.wynndie.spwallet.sharedCore.domain.error.ValidationError
import com.wynndie.spwallet.sharedCore.domain.validator.Validator

class CardNumberValidator : Validator<String> {

    override fun validate(value: String): Outcome<Boolean, ValidationError> {
        if (value.isBlank())
            return Outcome.Error(ValidationError.EMPTY_FIELD)

        if (!value.matches(Regex("^[0-9]+$")))
            return Outcome.Error(ValidationError.INVALID_CHARACTERS)

        if (value.length != 5)
            return Outcome.Error(ValidationError.EXACT_VALUE_REQUIRED)

        return Outcome.Success(true)
    }
}