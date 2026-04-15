package com.wynndie.spwallet.sharedFeature.transfer.domain.validators

import com.wynndie.spwallet.sharedCore.domain.constants.CoreConstants
import com.wynndie.spwallet.sharedCore.domain.outcome.Error
import com.wynndie.spwallet.sharedCore.domain.validators.Validator

class TransferCommentValidator : Validator<String> {

    override fun validate(value: String): Pair<Boolean, Error.Validation?> {
        if (value.length > CoreConstants.MAX_COMMENT_LENGTH)
            return false to Error.Validation.ABOVE_MAXIMUM_LENGTH

        return true to null
    }
}