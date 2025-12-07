package com.wynndie.spwallet.sharedFeature.home.domain.validators

import com.wynndie.spwallet.sharedCore.domain.error.ValidationError
import com.wynndie.spwallet.sharedCore.domain.validators.Validator

class UuidValidator : Validator<String> {

    override fun validate(value: String): Pair<Boolean, ValidationError?> {
        if (value.isBlank())
            return false to ValidationError.EMPTY_FIELD

        if (!uuidCharsRegex.matches(value))
            return false to ValidationError.INVALID_CHARACTERS

        if (!uuidFormatRegex.matches(value))
            return false to ValidationError.INVALID_FORMAT

        return true to null
    }

    companion object {
        private val uuidCharsRegex = Regex("^[0-9a-fA-F-]+$")
        private val uuidFormatRegex = Regex("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$")
    }
}