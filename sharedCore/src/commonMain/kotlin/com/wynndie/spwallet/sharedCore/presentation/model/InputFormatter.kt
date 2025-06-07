package com.wynndie.spwallet.sharedCore.presentation.model

import androidx.compose.ui.text.input.TextFieldValue

class InputFormatter(
    val value: TextFieldValue
) {

    fun filterBy(predicate: (Char) -> Boolean): InputFormatter {
        return InputFormatter(
            value = this.value.copy(
                text = this.value.text.filter(predicate = predicate)
            )
        )
    }

    fun dropFirst(predicate: String): InputFormatter {
        if (this.value.text.length <= predicate.length) return this
        if (!this.value.text.startsWith(predicate)) return this
        return InputFormatter(
            value = this.value.copy(
                text = this.value.text.drop(predicate.length)
            )
        )
    }

    fun cutOffAt(maxLength: Int): InputFormatter? {
        if (this.value.text.length <= maxLength) return this

        val newText = value.text.dropLast(value.text.length - maxLength)
        val textDelta = newText.length - value.text.length
        if (textDelta <= 1) return null

        return InputFormatter(
            value = this.value.copy(
                text = newText
            )
        )
    }
}
