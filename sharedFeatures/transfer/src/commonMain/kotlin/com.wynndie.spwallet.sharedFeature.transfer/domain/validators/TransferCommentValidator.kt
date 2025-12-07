package com.wynndie.spwallet.sharedFeature.transfer.domain.validators

import com.wynndie.spwallet.sharedCore.domain.error.ValidationError
import com.wynndie.spwallet.sharedCore.domain.validators.Validator

class TransferCommentValidator : Validator<String> {

    override fun validate(value: String): Pair<Boolean, ValidationError?> {
        return true to null
    }
}