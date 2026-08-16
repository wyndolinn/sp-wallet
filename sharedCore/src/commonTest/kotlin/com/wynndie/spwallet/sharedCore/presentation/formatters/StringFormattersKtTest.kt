package com.wynndie.spwallet.sharedCore.presentation.formatters

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test

class StringFormattersKtTest {

    @Test
    fun `number are being formatted correctly`() {
        assertThat("".asFormattedAmount()).isEqualTo("")
        assertThat("1000".asFormattedAmount()).isEqualTo("1000")
        assertThat("10000".asFormattedAmount()).isEqualTo("10 000")
        assertThat("-10000".asFormattedAmount()).isEqualTo("-10 000")
        assertThat("0,123456".asFormattedAmount()).isEqualTo("0.123456")
        assertThat(".123456".asFormattedAmount()).isEqualTo("0.123456")
    }
}