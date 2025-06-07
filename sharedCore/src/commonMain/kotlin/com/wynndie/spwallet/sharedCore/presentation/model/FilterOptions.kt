package com.wynndie.spwallet.sharedCore.presentation.model

sealed class FilterOptions(val predicate: (Char) -> Boolean) {

    sealed class Text(predicate: (Char) -> Boolean) : FilterOptions(predicate = predicate) {
        data object LettersOnly : Text(predicate = {
            it.isLetter()
        })
        data object LatinOnly : Text(predicate = {
            it in 'a'..'z' || it in 'A'..'Z'
        })
        data object LettersOrDigits : Text(predicate = {
            it.isLetterOrDigit()
        })
    }

    sealed class Digits(predicate: (Char) -> Boolean) : FilterOptions(predicate = predicate) {
        data object DigitsOnly : Digits(predicate = {
            it.isDigit()
        })
    }

    sealed class Structured(predicate: (Char) -> Boolean) : FilterOptions(predicate = predicate) {
        data object Uuid : Structured(predicate = {
            it.isDigit() || it in 'a'..'f' || it in 'A'..'F' || it == '-'
        })
        data object Base64 : Structured(predicate = {
            it.isDigit() || it in 'a'..'z' || it in 'A'..'Z' || it == '+' || it == '/' || it == '='
        })
    }
}