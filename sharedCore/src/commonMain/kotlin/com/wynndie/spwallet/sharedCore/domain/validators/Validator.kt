package com.wynndie.spwallet.sharedCore.domain.validators

import com.wynndie.spwallet.sharedCore.domain.outcome.Error

interface Validator<T> {
    fun validate(value: T): Pair<Boolean, Error.Validation?>
}