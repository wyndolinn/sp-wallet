package com.wynndie.spwallet.sharedCore.presentation.formatters.input

enum class InputFilters(val predicate: (Char) -> Boolean) {
    LettersOrDigits({ it.isLetterOrDigit() }),
    DigitsOnly({ it.isDigit() }),
    Uuid({ it.isDigit() || it in 'a'..'f' || it in 'A'..'F' || it == '-' }),
    Base64({ it.isDigit() || it in 'a'..'z' || it in 'A'..'Z' || it == '+' || it == '/' || it == '=' }),
}