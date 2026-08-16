package com.wynndie.spwallet.sharedCore.presentation.formatters

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test

class AsFormattedAmountKtTest {

    @Test
    fun `number are being formatted correctly`() {
        assertThat("".asFormattedAmount()).isEqualTo("")
        assertThat("1000".asFormattedAmount()).isEqualTo("1000")
        assertThat("10000".asFormattedAmount()).isEqualTo("10 000")
        assertThat("-10000".asFormattedAmount()).isEqualTo("-10 000")
        assertThat("0,123456".asFormattedAmount()).isEqualTo("0.123456")
        assertThat(".123456".asFormattedAmount()).isEqualTo("0.123456")
    }

    @Test
    fun `string with multiple whole numbers is formatted`() {
        val subject = "10000 шлк 37777 ст 4000000 ар"
        val expected = "10 000 шлк 37 777 ст 4 000 000 ар"
        assertThat(subject.asFormattedAmount()).isEqualTo(expected)
    }

    @Test
    fun `string with multiple decimal numbers is formatted`() {
        val subject = "10000.00 шлк 37777.55000 ст 4000000.11 ар"
        val expected = "10 000.00 шлк 37 777.55000 ст 4 000 000.11 ар"
        assertThat(subject.asFormattedAmount()).isEqualTo(expected)
    }
}