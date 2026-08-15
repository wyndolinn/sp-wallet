package com.wynndie.spwallet.sharedFeature.transfer.domain.validators

import com.wynndie.spwallet.sharedCore.domain.outcome.Error
import com.wynndie.spwallet.sharedCore.domain.validators.core.ValidationChain
import com.wynndie.spwallet.sharedCore.domain.validators.core.ValidationValues
import com.wynndie.spwallet.sharedCore.domain.validators.core.Validator

class TransferCommentValidator : Validator {
    override fun validate(value: ValidationValues): Pair<Boolean, Error.Validation?> {
        return ValidationChain(value.value)
            .ensureNotEmpty()
            .build()
    }
}