package com.wynndie.spwallet.sharedCore.presentation.extensions

import androidx.compose.ui.text.input.TextFieldValue
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import com.wynndie.spwallet.sharedCore.presentation.models.InputFilters
import kotlin.test.Test

class TextFieldValueTest {

    @Test
    fun `filter should apply input filter predicate to text`() {
        val value = TextFieldValue("123a b#")
        val filtered = value.filter(InputFilters.Numbers)
        assertThat(filtered.text).isEqualTo("123")
    }

    @Test
    fun `trimSpaces should replace multiple spaces with single space`() {
        val value = TextFieldValue("hello   world   test")
        val trimmed = value.trimSpaces()
        assertThat(trimmed.text).isEqualTo("hello world test")
    }

    @Test
    fun `dropWhile should drop leading characters matching char and predicate`() {
        val value = TextFieldValue("000123")
        val dropped = value.dropWhile('0') { it.startsWith("0") }
        assertThat(dropped.text).isEqualTo("123")
    }

    @Test
    fun `dropWhile should return original when text does not start with char`() {
        val value = TextFieldValue("123000")
        val dropped = value.dropWhile('0')
        assertThat(dropped.text).isEqualTo("123000")
    }

    @Test
    fun `dropWhile should return original when predicate returns false`() {
        val value = TextFieldValue("000123")
        val dropped = value.dropWhile('0') { false }
        assertThat(dropped.text).isEqualTo("000123")
    }

    @Test
    fun `cutOffAt should return original when text length is within maxLength`() {
        val value = TextFieldValue("123")
        val result = value.cutOffAt(5)
        assertThat(result).isNotNull()
        assertThat(result?.text).isEqualTo("123")
    }

    @Test
    fun `cutOffAt should return null when text length exceeds maxLength according to delta logic`() {
        val value = TextFieldValue("12345")
        val result = value.cutOffAt(3)
        assertThat(result).isNull()
    }
}
