package com.wynndie.spwallet.sharedCore.presentation.formatters.input

import androidx.compose.ui.text.input.TextFieldValue

fun TextFieldValue.filterBy(predicate: (Char) -> Boolean): TextFieldValue {
    return this.copy(text = this.text.filter(predicate = predicate))
}

internal fun TextFieldValue.replace(oldChar: String, newChar: String): TextFieldValue {
    return this.copy(text = this.text.replace(oldChar, newChar))
}

fun TextFieldValue.dropFirst(char: Char): TextFieldValue {
    if (!this.text.startsWith(char)) return this
    return this.copy(text = this.text.dropWhile { it == char })
}

fun TextFieldValue.replaceBeginning(oldValue: String, newValue: String): TextFieldValue {
    if (!this.text.startsWith(oldValue)) return this
    return this.copy(text = this.text.replaceFirst(oldValue, newValue))
}

fun TextFieldValue.replaceBeginning(oldValue: Char, newValue: Char): TextFieldValue {
    if (!this.text.startsWith(oldValue)) return this
    return this.copy(text = this.text.replaceFirst(oldValue, newValue))
}

fun TextFieldValue.cutOffAt(maxLength: Int): TextFieldValue? {
    if (this.text.length <= maxLength) return this

    val newText = this.text.dropLast(this.text.length - maxLength)
    val textDelta = newText.length - this.text.length
    if (textDelta <= 1) return null

    return this.copy(text = newText)
}
