package com.wynndie.spwallet.sharedCore.domain.outcome

sealed interface Error {
    enum class Network : Error {
        BAD_REQUEST,
        UNAUTHORIZED,
        FORBIDDEN,
        NOT_FOUND,
        REQUEST_TIMEOUT,
        TOO_MANY_REQUESTS,
        SERVER_ERROR,
        NO_INTERNET,
        SERIALIZATION_ERROR,
        UNKNOWN
    }

    enum class Validation : Error {
        EMPTY_FIELD,
        INVALID_CHARACTERS,
        INVALID_FORMAT,
        BELOW_MINIMUM_VALUE,
        ABOVE_MAXIMUM_VALUE,
        EXACT_VALUE_REQUIRED,
        BELOW_MINIMUM_LENGTH,
        ABOVE_MAXIMUM_LENGTH,
        EXACT_LENGTH_REQUIRED
    }
}