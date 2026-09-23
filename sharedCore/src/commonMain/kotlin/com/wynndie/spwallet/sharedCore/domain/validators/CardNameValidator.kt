package com.wynndie.spwallet.sharedCore.domain.validators

import com.wynndie.spwallet.sharedCore.domain.outcome.Error
import com.wynndie.spwallet.sharedCore.domain.validators.core.ValidationChain
import com.wynndie.spwallet.sharedCore.domain.validators.core.ValidationValues
import com.wynndie.spwallet.sharedCore.domain.validators.core.Validator

class CardNameValidator : Validator {
    override fun validate(value: ValidationValues): Pair<Boolean, Error.Validation?> {
        return ValidationChain(value.value)
            .ensureNotEmpty()
            .build()
    }
}