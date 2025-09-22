package com.wynndie.spwallet.sharedCore.domain.validator

import com.wynndie.spwallet.sharedCore.domain.error.ValidationError

interface Validator<T> {
    fun validate(value: T): Pair<Boolean, ValidationError?>
}