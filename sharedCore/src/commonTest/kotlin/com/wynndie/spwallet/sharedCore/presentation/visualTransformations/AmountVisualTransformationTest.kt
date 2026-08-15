package com.wynndie.spwallet.sharedCore.presentation.visualTransformations

import androidx.compose.ui.text.AnnotatedString
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.wynndie.spwallet.sharedCore.presentation.formatters.asFormattedAmount
import kotlin.test.Test

class AmountVisualTransformationTest {

    private val transformation = AmountVisualTransformation()

    @Test
    fun `filter should return formatted text and valid offset mapping`() {
        val original = "10000"
        val transformedText = transformation.filter(AnnotatedString(original))

        assertThat(transformedText.text.text).isEqualTo(original.asFormattedAmount())

        val offsetMapping = transformedText.offsetMapping
        assertThat(offsetMapping.originalToTransformed(0)).isEqualTo(0)
    }
}
