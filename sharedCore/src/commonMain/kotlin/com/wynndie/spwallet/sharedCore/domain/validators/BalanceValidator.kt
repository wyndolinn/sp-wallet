package com.wynndie.spwallet.sharedCore.domain.validators

import com.wynndie.spwallet.sharedCore.domain.error.ValidationError
import com.wynndie.spwallet.sharedCore.domain.validators.models.BalanceValidationValues

class BalanceValidator : Validator<BalanceValidationValues> {

    override fun validate(value: BalanceValidationValues): Pair<Boolean, ValidationError?> {
        if (value.value.isBlank())
            return false to ValidationError.EMPTY_FIELD

        if (!value.value.matches(Regex("^[0-9]+$")))
            return false to ValidationError.INVALID_CHARACTERS

        if (value.value.toLong() < value.minValue)
            return false to ValidationError.BELOW_MINIMUM_VALUE

        if (value.value.toLong() > value.maxValue)
            return false to ValidationError.ABOVE_MAXIMUM_VALUE

        return true to null
    }
}