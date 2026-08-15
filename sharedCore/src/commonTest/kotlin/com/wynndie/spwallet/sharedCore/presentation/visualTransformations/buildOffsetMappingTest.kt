package com.wynndie.spwallet.sharedCore.presentation.visualTransformations

import androidx.compose.ui.text.input.OffsetMapping
import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test

class BuildOffsetMappingKtTest {

    @Test
    fun `identical original and transformed text produce identity mapping in both directions`() {
        val mapping = buildOffsetMapping(original = "abc", transformed = "abc")

        val transformedText = getStringFromOriginalMap(mapping, "abc")
        val originalText = getStringFromTransformedMap(mapping, "abc")

        assertThat(transformedText).isEqualTo("0,1,2")
        assertThat(originalText).isEqualTo("0,1,2")
    }

    @Test
    fun `mapping accounts for characters inserted by formatting`() {
        val mapping = buildOffsetMapping(original = "1234567890", transformed = "(123) 456-7890")

        val transformedText = getStringFromOriginalMap(mapping, "(123) 456-7890")
        val originalText = getStringFromTransformedMap(mapping, "1234567890")

        assertThat(transformedText).isEqualTo("1,2,3,6,7,8,10,11,12,13")
        assertThat(originalText).isEqualTo("0,0,1,2,3,3,3,4,5,6,6,7,8,9")
    }

    @Test
    fun `when transformed text is longer than original, transformedToOriginal clamps to original text length`() {
        val mapping = buildOffsetMapping(original = "a", transformed = "abc")

        val transformedText = getStringFromOriginalMap(mapping, "abc")
        val originalText = getStringFromTransformedMap(mapping, "a")

        assertThat(transformedText).isEqualTo("0,1,1")
        assertThat(originalText).isEqualTo("0")
    }

    @Test
    fun `when transformed text is shorter than original, originalToTransformed clamps to transformed text length`() {
        val mapping = buildOffsetMapping(original = "abc", transformed = "a")

        val originalText = getStringFromTransformedMap(mapping, "abc")
        val transformedText = getStringFromOriginalMap(mapping, "a")

        assertThat(transformedText).isEqualTo("0")
        assertThat(originalText).isEqualTo("0,1,1")
    }

    @Test
    fun `out of bounds offsets fall back to the corresponding text length`() {
        val mapping = buildOffsetMapping(original = "aaaa", transformed = "aaaaaa")

        assertThat(mapping.originalToTransformed(1000)).isEqualTo(6)
        assertThat(mapping.originalToTransformed(-1)).isEqualTo(6)

        assertThat(mapping.transformedToOriginal(1000)).isEqualTo(4)
        assertThat(mapping.transformedToOriginal(-1)).isEqualTo(4)
    }

    private fun getStringFromTransformedMap(
        mapping: OffsetMapping,
        original: String
    ): String {
        return buildList {
            (0..original.lastIndex).forEach { index ->
                add(mapping.originalToTransformed(index))
            }
        }.joinToString(",")
    }

    private fun getStringFromOriginalMap(
        mapping: OffsetMapping,
        transformed: String
    ): String {
        return buildList {
            (0..(transformed.lastIndex)).forEach { index ->
                add(mapping.transformedToOriginal(index))
            }
        }.joinToString(",")
    }
}