package com.wynndie.spwallet.sharedCore.domain.validators

import com.wynndie.spwallet.sharedCore.domain.outcome.Error
import com.wynndie.spwallet.sharedCore.domain.validators.models.BalanceValidationValues

class BalanceValidator : Validator<BalanceValidationValues> {

    override fun validate(value: BalanceValidationValues): Pair<Boolean, Error.Validation?> {
        if (value.value.isBlank())
            return false to Error.Validation.EMPTY_FIELD

        if (!value.value.matches(Regex("^[0-9]+$")))
            return false to Error.Validation.INVALID_CHARACTERS

        if (value.value.toLong() < value.minValue)
            return false to Error.Validation.BELOW_MINIMUM_VALUE

        if (value.value.toLong() > value.maxValue)
            return false to Error.Validation.ABOVE_MAXIMUM_VALUE

        return true to null
    }
}