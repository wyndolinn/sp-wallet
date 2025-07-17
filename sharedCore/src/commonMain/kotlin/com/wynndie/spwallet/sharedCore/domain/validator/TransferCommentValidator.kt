package com.wynndie.spwallet.sharedCore.domain.validator

import com.wynndie.spwallet.sharedCore.domain.error.ValidationError

class TransferCommentValidator : Validator<String> {

    override fun validate(value: String): Pair<Boolean, ValidationError?> {
        return true to null
    }
}