package com.wynndie.spwallet.sharedCore.presentation.visualTransformations

import androidx.compose.ui.text.input.OffsetMapping

/**
 * Строит [OffsetMapping] между исходным текстом и его отформатированной версией
 *
 * Сопоставляет символы [rawText] с их позициями в [formattedText] путём
 * последовательного посимвольного сравнения, учитывая добавленные символы
 *
 * Функция предполагает, что [rawText] является подпоследовательностью
 * [formattedText] - то есть форматирование только добавляет символы и не
 * переставляет и не удаляет символы исходного текста. Если это условие не
 * выполняется, результат сопоставления не гарантируется
 *
 * @param rawText исходный текст до форматирования
 * @param formattedText результат форматирования [rawText]
 * @return [OffsetMapping], преобразующий смещения курсора между [rawText] и [formattedText] в обе стороны
 */
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