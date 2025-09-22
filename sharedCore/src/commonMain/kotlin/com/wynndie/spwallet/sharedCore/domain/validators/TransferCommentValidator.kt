package com.wynndie.spwallet.sharedCore.domain.validators

import com.wynndie.spwallet.sharedCore.domain.error.ValidationError

class TransferCommentValidator : Validator<String> {

    override fun validate(value: String): Pair<Boolean, ValidationError?> {
        return true to null
    }
}