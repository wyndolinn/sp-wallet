package com.wynndie.spwallet.sharedCore.domain.validators.core

import com.wynndie.spwallet.sharedCore.domain.outcome.Error

interface Validator {
    fun validate(value: ValidationValues): Pair<Boolean, Error.Validation?>
}