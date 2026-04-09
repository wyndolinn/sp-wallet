package com.wynndie.spwallet.sharedCore.presentation.formatters.input

sealed class InputFilterOptions(val predicate: (Char) -> Boolean) {

    data object LettersOrDigits : InputFilterOptions(predicate = {
        it.isLetterOrDigit()
    })

    data object DigitsOnly : InputFilterOptions(predicate = {
        it.isDigit()
    })

    data object Uuid : InputFilterOptions(predicate = {
        it.isDigit() || it in 'a'..'f' || it in 'A'..'F' || it == '-'
    })

    data object Base64 : InputFilterOptions(predicate = {
        it.isDigit() || it in 'a'..'z' || it in 'A'..'Z' || it == '+' || it == '/' || it == '='
    })
}