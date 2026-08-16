package com.wynndie.spwallet.sharedCore.presentation.formatters

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.wynndie.spwallet.sharedCore.Res
import com.wynndie.spwallet.sharedCore.presentation.models.UiText
import com.wynndie.spwallet.sharedCore.x_of_ore
import com.wynndie.spwallet.sharedCore.x_of_shulkers
import com.wynndie.spwallet.sharedCore.x_of_stacks
import kotlin.test.Test

class DisplayableOreValueTest {

    @Test
    fun `zero value returns zero`() {
        assertThat(DisplayableOreValue.of(0)).isEqualTo(DisplayableOreValue(0, emptyList()))
    }

    @Test
    fun `less than a stack returns empty formatted`() {
        assertThat(DisplayableOreValue.of(54)).isEqualTo(DisplayableOreValue(54, emptyList()))
    }

    @Test
    fun `formatted maps correctly`() {
        val result1 = DisplayableOreValue.of(64L).formatted
            .filterIsInstance<UiText.ResourceString>()
            .map { it.id to it.args.toList() }
        assertThat(result1).isEqualTo(listOf(Res.string.x_of_stacks to listOf(1L)))

        val result2 = DisplayableOreValue.of(64L * 27L).formatted
            .filterIsInstance<UiText.ResourceString>()
            .map { it.id to it.args.toList() }
        assertThat(result2).isEqualTo(listOf(Res.string.x_of_shulkers to listOf(1L)))

        val result3 = DisplayableOreValue.of(64L * 27L + 64L).formatted
            .filterIsInstance<UiText.ResourceString>()
            .map { it.id to it.args.toList() }
        assertThat(result3).isEqualTo(
            listOf(Res.string.x_of_shulkers to listOf(1L), Res.string.x_of_stacks to listOf(1L))
        )

        val result4 = DisplayableOreValue.of(64L * 27L + 54L).formatted
            .filterIsInstance<UiText.ResourceString>()
            .map { it.id to it.args.toList() }
        assertThat(result4).isEqualTo(
            listOf(Res.string.x_of_shulkers to listOf(1L), Res.string.x_of_ore to listOf(54L))
        )
    }
}