package com.wynndie.spwallet.sharedCore.presentation.extensions

import androidx.compose.ui.text.input.TextFieldValue
import com.wynndie.spwallet.sharedCore.presentation.models.InputFilters

fun TextFieldValue.filter(filter: InputFilters): TextFieldValue {
    return this.copy(text = this.text.filter(predicate = filter.predicate))
}

fun TextFieldValue.trimSpaces(): TextFieldValue {
    return this.copy(text = this.text.replace(Regex("\\s+"), " "))
}

fun TextFieldValue.dropWhile(
    char: Char,
    predicate: (String) -> Boolean = { true }
): TextFieldValue {
    if (!this.text.startsWith(char)) return this
    if (!predicate(this.text)) return this
    return this.copy(text = this.text.dropWhile { it == char })
}

fun TextFieldValue.cutOffAt(maxLength: Int): TextFieldValue? {
    if (this.text.length <= maxLength) return this

    val newText = this.text.dropLast(this.text.length - maxLength)
    val textDelta = newText.length - this.text.length
    if (textDelta <= 1) return null

    return this.copy(text = newText)
}
