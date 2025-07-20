package com.wynndie.spwallet.sharedCore.presentation.model.input

import androidx.compose.ui.text.input.TextFieldValue

fun TextFieldValue.filterBy(predicate: (Char) -> Boolean): TextFieldValue {
    return this.copy(text = this.text.filter(predicate = predicate))
}

fun TextFieldValue.dropFirst(predicate: String): TextFieldValue {
    if (this.text.length <= predicate.length) return this
    if (!this.text.startsWith(predicate)) return this
    return this.copy(text = this.text.drop(predicate.length))
}

fun TextFieldValue.cutOffAt(maxLength: Int): TextFieldValue? {
    if (this.text.length <= maxLength) return this

    val newText = this.text.dropLast(this.text.length - maxLength)
    val textDelta = newText.length - this.text.length
    if (textDelta <= 1) return null

    return this.copy(text = newText)
}
