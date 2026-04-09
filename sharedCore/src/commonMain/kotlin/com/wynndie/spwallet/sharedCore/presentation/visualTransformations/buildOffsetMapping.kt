package com.wynndie.spwallet.sharedCore.presentation.visualTransformations

import androidx.compose.ui.text.input.OffsetMapping

fun buildOffsetMapping(rawText: String, formattedText: String): OffsetMapping {

    val originalToTransformedOffsets = mutableListOf<Int>()
    val transformedToOriginalOffsets = mutableListOf<Int>()

    var rawIndex = 0
    formattedText.forEachIndexed { index, char ->
        val isRawCharInBounds = rawIndex < rawText.length
        val isSameChar = isRawCharInBounds && char == rawText[rawIndex]

        transformedToOriginalOffsets.add(rawIndex)
        if (isSameChar) {
            originalToTransformedOffsets.add(index)
            rawIndex++
        }
    }

    originalToTransformedOffsets.add(formattedText.length)
    transformedToOriginalOffsets.add(rawText.length)

    return object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            return originalToTransformedOffsets.getOrElse(offset) { formattedText.length }
        }

        override fun transformedToOriginal(offset: Int): Int {
            return transformedToOriginalOffsets.getOrElse(offset) { rawText.length }
        }
    }
}