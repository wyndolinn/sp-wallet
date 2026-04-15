package com.wynndie.spwallet.sharedCore.presentation.visualTransformations

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.wynndie.spwallet.sharedCore.presentation.extensions.asFormattedAmount

internal class AmountVisualTransformation : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val rawText = text.text
        val formattedText = rawText.asFormattedAmount()

        val offsetMapping = buildOffsetMapping(rawText, formattedText)

        return TransformedText(AnnotatedString(formattedText), offsetMapping)
    }
}