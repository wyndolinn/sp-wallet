package com.wynndie.spwallet.sharedFeature.home.domain.validators

import com.wynndie.spwallet.sharedCore.domain.outcome.Error
import com.wynndie.spwallet.sharedCore.domain.validators.Validator

class TokenValidator : Validator<String> {

    override fun validate(value: String): Pair<Boolean, Error.Validation?> {
        if (value.isBlank())
            return false to Error.Validation.EMPTY_FIELD

        if (!value.matches(base64CharsRegex))
            return false to Error.Validation.INVALID_CHARACTERS

        if (!value.matches(base64FormatRegex))
            return false to Error.Validation.INVALID_FORMAT

        return true to null
    }

    companion object {
        private val base64CharsRegex = Regex("^[A-Za-z0-9+/=]+$")
        private val base64FormatRegex = Regex("^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?$")
    }
}