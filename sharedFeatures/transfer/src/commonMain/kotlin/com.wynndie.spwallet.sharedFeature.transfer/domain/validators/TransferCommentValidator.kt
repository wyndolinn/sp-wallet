package com.wynndie.spwallet.sharedFeature.transfer.domain.validators

import com.wynndie.spwallet.sharedCore.domain.constants.CoreConstants
import com.wynndie.spwallet.sharedCore.domain.error.ValidationError
import com.wynndie.spwallet.sharedCore.domain.validators.Validator

class TransferCommentValidator : Validator<String> {

    override fun validate(value: String): Pair<Boolean, ValidationError?> {
        if (value.length > CoreConstants.MAX_COMMENT_LENGTH)
            return false to ValidationError.ABOVE_MAXIMUM_LENGTH

        return true to null
    }
}