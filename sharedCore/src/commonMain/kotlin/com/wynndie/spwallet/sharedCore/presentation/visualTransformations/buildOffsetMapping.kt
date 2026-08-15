package com.wynndie.spwallet.sharedCore.presentation.visualTransformations

import androidx.compose.ui.text.input.OffsetMapping

/**
 * Строит [OffsetMapping] между исходным текстом и его отформатированной версией
 *
 * Сопоставляет символы [original] с их позициями в [transformed] путём
 * последовательного посимвольного сравнения, учитывая добавленные символы
 *
 * Функция предполагает, что [original] является подпоследовательностью
 * [transformed] - то есть форматирование только добавляет символы и не
 * переставляет и не удаляет символы исходного текста. Если это условие не
 * выполняется, результат сопоставления не гарантируется
 *
 * @param original исходный текст до форматирования
 * @param transformed результат форматирования [original]
 * @return [OffsetMapping], преобразующий смещения курсора между [original] и [transformed] в обе стороны
 */
fun buildOffsetMapping(original: String, transformed: String): OffsetMapping {

    val originalToTransformedOffsets = mutableListOf<Int>()
    val transformedToOriginalOffsets = mutableListOf<Int>()

    var originalCharIndex = 0
    transformed.forEachIndexed { transformedCharIndex, char ->
        val isOriginalCharInBounds = originalCharIndex < original.length
        val isSameChar = isOriginalCharInBounds && char == original[originalCharIndex]

        transformedToOriginalOffsets.add(originalCharIndex)
        if (isSameChar) {
            originalToTransformedOffsets.add(transformedCharIndex)
            originalCharIndex++
        }
    }

    originalToTransformedOffsets.add(transformed.length)
    transformedToOriginalOffsets.add(original.length)

    return object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            return originalToTransformedOffsets.getOrElse(offset) { transformed.length }
        }

        override fun transformedToOriginal(offset: Int): Int {
            return transformedToOriginalOffsets.getOrElse(offset) { original.length }
        }
    }
}