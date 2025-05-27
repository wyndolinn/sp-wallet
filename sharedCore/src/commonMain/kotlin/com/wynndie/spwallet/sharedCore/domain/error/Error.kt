package com.wynndie.spwallet.sharedCore.domain.error

sealed interface Error

sealed interface DataError : Error {
    enum class Remote : DataError {
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

    enum class Local : DataError {
        DISK_FULL,
        EMPTY_TABLE,
        NO_MATCH_FOUND
    }
}

enum class ValidationError : Error {
    EMPTY_FIELD,
    BELOW_MINIMUM,
    ABOVE_MAXIMUM,
    EXACT_VALUE_REQUIRED,
    INVALID_CHARACTERS,
    INSUFFICIENT_VALUE
}