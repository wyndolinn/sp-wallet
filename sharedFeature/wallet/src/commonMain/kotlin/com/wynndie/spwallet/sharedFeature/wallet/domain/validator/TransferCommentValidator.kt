package com.wynndie.spwallet.sharedFeature.wallet.domain.validator

import com.wynndie.spwallet.sharedCore.domain.error.Outcome
import com.wynndie.spwallet.sharedCore.domain.error.ValidationError
import com.wynndie.spwallet.sharedCore.domain.validator.Validator

class TransferCommentValidator : Validator<String> {

    override fun validate(value: String): Outcome<Boolean, ValidationError> {
        return Outcome.Success(true)
    }
}