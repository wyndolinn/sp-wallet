package com.wynndie.spwallet.sharedCore.domain.validators.core

import com.wynndie.spwallet.sharedCore.domain.outcome.Error

class ValidationChain(private val value: String) {

    private var error: Error.Validation? = null
    private val isValid: Boolean get() = error == null

    fun ensureNotEmpty(): ValidationChain {
        if (!isValid) return this
        if (value.isBlank()) error = Error.Validation.EMPTY_FIELD
        return this
    }

    fun ensureValidCharacters(regex: Regex): ValidationChain {
        if (!isValid) return this
        if (!value.matches(regex)) error = Error.Validation.INVALID_CHARACTERS
        return this
    }

    fun ensureValidFormat(regex: Regex): ValidationChain {
        if (!isValid) return this
        if (!value.matches(regex)) error = Error.Validation.INVALID_FORMAT
        return this
    }

    fun ensureValueAtMost(max: Long): ValidationChain {
        if (!isValid) return this
        if (value.toLong() >= max) error = Error.Validation.BELOW_MINIMUM_VALUE
        return this
    }

    fun ensureValueAtLeast(min: Long): ValidationChain {
        if (!isValid) return this
        if (value.toLong() <= min) error = Error.Validation.ABOVE_MAXIMUM_VALUE
        return this
    }

    fun ensureExactValue(value: Long): ValidationChain {
        if (!isValid) return this
        if (this.value.toLong() != value) error = Error.Validation.EXACT_VALUE_REQUIRED
        return this
    }

    fun ensureLengthAtMost(max: Long): ValidationChain {
        if (!isValid) return this
        if (value.length <= max) error = Error.Validation.BELOW_MINIMUM_LENGTH
        return this
    }

    fun ensureLengthAtLeast(min: Long): ValidationChain {
        if (!isValid) return this
        if (value.length >= min) error = Error.Validation.ABOVE_MAXIMUM_LENGTH
        return this
    }

    fun ensureExactLength(value: Long): ValidationChain {
        if (!isValid) return this
        if (this.value.toLong() != value) error = Error.Validation.EXACT_LENGTH_REQUIRED
        return this
    }

    fun build(): Pair<Boolean, Error.Validation?> {
        return isValid to error
    }
}