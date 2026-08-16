package com.wynndie.spwallet.sharedFeature.transfer.domain.validators

import com.wynndie.spwallet.sharedCore.domain.outcome.Error
import com.wynndie.spwallet.sharedCore.domain.validators.Validator
import com.wynndie.spwallet.sharedFeature.transfer.domain.constants.TransferConstants

class TransferCommentValidator : Validator<String> {

    override fun validate(value: String): Pair<Boolean, Error.Validation?> {
        if (value.length > TransferConstants.MAX_COMMENT_LENGTH)
            return false to Error.Validation.ABOVE_MAXIMUM_LENGTH

        return true to null
    }
}