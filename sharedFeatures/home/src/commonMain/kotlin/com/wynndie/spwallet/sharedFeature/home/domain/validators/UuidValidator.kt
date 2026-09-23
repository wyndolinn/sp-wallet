package com.wynndie.spwallet.sharedFeature.home.domain.validators

import com.wynndie.spwallet.sharedCore.domain.outcome.Error
import com.wynndie.spwallet.sharedCore.domain.validators.core.ValidationChain
import com.wynndie.spwallet.sharedCore.domain.validators.core.ValidationValues
import com.wynndie.spwallet.sharedCore.domain.validators.core.Validator

class UuidValidator : Validator {
    override fun validate(value: ValidationValues): Pair<Boolean, Error.Validation?> {
        return ValidationChain(value.value)
            .ensureNotEmpty()
            .ensureValidCharacters(uuidCharsRegex)
            .ensureValidFormat(uuidFormatRegex)
            .build()
    }

    companion object {
        private val uuidCharsRegex = Regex("^[0-9a-fA-F-]+$")
        private val uuidFormatRegex =
            Regex("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$")
    }
}