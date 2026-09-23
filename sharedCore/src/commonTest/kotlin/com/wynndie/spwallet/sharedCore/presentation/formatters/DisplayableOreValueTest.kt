package com.wynndie.spwallet.sharedCore.presentation.formatters

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import com.wynndie.spwallet.sharedCore.Res
import com.wynndie.spwallet.sharedCore.presentation.models.UiText
import com.wynndie.spwallet.sharedCore.x_of_ore
import com.wynndie.spwallet.sharedCore.x_of_shulkers
import com.wynndie.spwallet.sharedCore.x_of_stacks
import kotlin.test.Test

class DisplayableOreValueTest {

    @Test
    fun `of should return empty values for the value of 0`() {
        assertThat(DisplayableOreValue.of(0)).isEqualTo(DisplayableOreValue(0, emptyList()))
    }

    @Test
    fun `of should return empty formatted list when value is less than a stack`() {
        assertThat(DisplayableOreValue.of(54)).isEqualTo(DisplayableOreValue(54, emptyList()))
    }

    @Test
    fun `of should correctly format value equal to exactly one stack`() {
        val result = DisplayableOreValue.of(64L).formatted

        assertThat(result).hasSize(1)
        val first = result[0] as UiText.ResourceString
        assertThat(first.id).isEqualTo(Res.string.x_of_stacks)
        assertThat(first.args[0]).isEqualTo(1L)
    }

    @Test
    fun `of should correctly format value equal to exactly one shulker`() {
        val result = DisplayableOreValue.of(64L * 27L).formatted

        assertThat(result).hasSize(1)
        val first = result[0] as UiText.ResourceString
        assertThat(first.id).isEqualTo(Res.string.x_of_shulkers)
        assertThat(first.args[0]).isEqualTo(1L)
    }

    @Test
    fun `of should correctly format value with shulker and exactly one stack`() {
        val result = DisplayableOreValue.of(64L * 27L + 64L).formatted

        assertThat(result).hasSize(2)

        val first = result[0] as UiText.ResourceString
        assertThat(first.id).isEqualTo(Res.string.x_of_shulkers)
        assertThat(first.args[0]).isEqualTo(1L)

        val second = result[1] as UiText.ResourceString
        assertThat(second.id).isEqualTo(Res.string.x_of_stacks)
        assertThat(second.args[0]).isEqualTo(1L)
    }

    @Test
    fun `of should correctly format value with shulker and remaining ore less than a stack`() {
        val result = DisplayableOreValue.of(64L * 27L + 54L).formatted

        assertThat(result).hasSize(2)

        val first = result[0] as UiText.ResourceString
        assertThat(first.id).isEqualTo(Res.string.x_of_shulkers)
        assertThat(first.args[0]).isEqualTo(1L)

        val second = result[1] as UiText.ResourceString
        assertThat(second.id).isEqualTo(Res.string.x_of_ore)
        assertThat(second.args[0]).isEqualTo(54L)
    }

    @Test
    fun `of should correctly format value with shulker, stack, and remaining ore`() {
        val result = DisplayableOreValue.of(64L * 27L + 64L + 54L).formatted

        assertThat(result).hasSize(3)

        val first = result[0] as UiText.ResourceString
        assertThat(first.id).isEqualTo(Res.string.x_of_shulkers)
        assertThat(first.args[0]).isEqualTo(1L)

        val second = result[1] as UiText.ResourceString
        assertThat(second.id).isEqualTo(Res.string.x_of_stacks)
        assertThat(second.args[0]).isEqualTo(1L)

        val third = result[2] as UiText.ResourceString
        assertThat(third.id).isEqualTo(Res.string.x_of_ore)
        assertThat(third.args[0]).isEqualTo(54L)
    }
}
