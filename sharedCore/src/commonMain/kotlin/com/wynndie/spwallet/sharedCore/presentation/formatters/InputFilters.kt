package com.wynndie.spwallet.sharedCore.presentation.formatters

enum class InputFilters(val predicate: (Char) -> Boolean) {
    PlainText({ it.isAlphabet() || it.isDigit() || it.isWhitespace() }),
    Decimals({ it.isDigit() }),
    Uuid({ it.isHexadecimal() || it == '-' }),
    Base64({ it.isDigit() || it.isLatin() || it == '+' || it == '/' || it == '=' })
}

private fun Char.isHexadecimal(): Boolean {
    return this.isDigit() || this in 'a'..'f' || this in 'A'..'F'
}

private fun Char.isLatin(): Boolean {
    return this in 'a'..'z' || this in 'A'..'Z'
}

private fun Char.isAlphabet(): Boolean {
    return this in 'а'..'я' || this in 'А'..'Я' || this.isLatin()
}