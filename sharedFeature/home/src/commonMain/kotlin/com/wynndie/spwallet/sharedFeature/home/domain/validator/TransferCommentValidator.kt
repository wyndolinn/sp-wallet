package com.wynndie.spwallet.sharedFeature.home.domain.validator

import com.wynndie.spwallet.sharedCore.domain.error.ValidationError
import com.wynndie.spwallet.sharedCore.domain.validator.Validator

class TransferCommentValidator : Validator<String> {

    override fun validate(value: String): Pair<Boolean, ValidationError?> {
        return true to null
    }
}