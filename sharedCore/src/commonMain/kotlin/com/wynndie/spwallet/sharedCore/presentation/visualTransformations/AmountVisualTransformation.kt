package com.wynndie.spwallet.sharedCore.presentation.visualTransformations

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.wynndie.spwallet.sharedCore.presentation.formatters.asFormattedAmount

internal class AmountVisualTransformation : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text
        val formattedText = originalText.asFormattedAmount()

        val offsetMapping = buildOffsetMapping(originalText, formattedText)

        return TransformedText(AnnotatedString(formattedText), offsetMapping)
    }
}