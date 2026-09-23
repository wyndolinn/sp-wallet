package com.wynndie.spwallet.sharedCore.presentation.extensions

fun Char.isHexadecimal(): Boolean {
    return this.isDigit() || this in 'a'..'f' || this in 'A'..'F'
}

fun Char.isLatin(): Boolean {
    return this in 'a'..'z' || this in 'A'..'Z'
}

fun Char.isAlphabet(): Boolean {
    return this in 'а'..'я' || this in 'А'..'Я' || this.isLatin()
}