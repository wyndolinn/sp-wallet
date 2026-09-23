package com.wynndie.spwallet.sharedFeature.home.domain.validators

import com.wynndie.spwallet.sharedCore.domain.outcome.Error
import com.wynndie.spwallet.sharedCore.domain.validators.core.ValidationChain
import com.wynndie.spwallet.sharedCore.domain.validators.core.ValidationValues
import com.wynndie.spwallet.sharedCore.domain.validators.core.Validator

class TokenValidator : Validator {
    override fun validate(value: ValidationValues): Pair<Boolean, Error.Validation?> {
        return ValidationChain(value.value)
            .ensureNotEmpty()
            .ensureValidCharacters(base64CharsRegex)
            .ensureValidFormat(base64FormatRegex)
            .build()
    }

    companion object {
        private val base64CharsRegex = Regex("^[A-Za-z0-9+/=]+$")
        private val base64FormatRegex =
            Regex("^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?$")
    }
}