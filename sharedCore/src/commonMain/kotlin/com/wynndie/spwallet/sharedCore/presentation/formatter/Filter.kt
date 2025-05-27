package com.wynndie.spwallet.sharedCore.presentation.formatter

sealed interface Filter

enum class LettersFilter(val predicate: (Char) -> Boolean) : Filter {
    LETTERS_ONLY(predicate = {
        it.isLetter()
    }),
    LATIN_ONLY(predicate = {
        it in 'a'..'z' || it in 'A'..'Z'
    })
}

enum class DigitsFilter(val predicate: (Char) -> Boolean) : Filter {
    DIGITS_ONLY(predicate = {
        it.isDigit()
    })
}

enum class StructuredFilter(val predicate: (Char) -> Boolean) : Filter {
    UUID(predicate = {
        it.isDigit() || it in 'a'..'f' || it in 'A'..'F' || it == '-'
    }),
    BASE64(predicate = {
        it.isDigit() || it in 'a'..'z' || it in 'A'..'Z' || it == '+' || it == '/' || it == '='
    })
}

enum class TextFilter(val predicate: (Char) -> Boolean) : Filter {
    LETTERS_OR_DIGITS(predicate = {
        it.isLetterOrDigit()
    })
}