package com.wynndie.spwallet.sharedCore.presentation.formatters

import androidx.compose.runtime.Composable
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.x_of_ore
import com.wynndie.spwallet.sharedResources.x_of_shulkers
import com.wynndie.spwallet.sharedResources.x_of_stacks
import kotlin.collections.joinToString

data class DisplayableOreValue(
    val value: Long,
    val formatted: String
) {

    companion object {
        private const val ORE_IN_STACK = 64
        private const val STACKS_IN_SHULKER = 27

        @Composable
        fun of(value: Long): DisplayableOreValue {
            if (value == 0L) {
                return DisplayableOreValue(
                    value = value,
                    formatted = UiText.ResourceString(Res.string.x_of_ore, value).asString()
                )
            }

            val shulkers = value / ORE_IN_STACK / STACKS_IN_SHULKER
            val stacks = value / ORE_IN_STACK % STACKS_IN_SHULKER
            val ore = value % ORE_IN_STACK

            val formattedValue = buildList {
                if (shulkers > 0) add(UiText.ResourceString(Res.string.x_of_shulkers, shulkers))
                if (stacks > 0) add(UiText.ResourceString(Res.string.x_of_stacks, stacks))
                if (ore > 0) add(UiText.ResourceString(Res.string.x_of_ore, ore))
            }.map { it.asString() }.joinToString(" ")

            return DisplayableOreValue(
                value = value,
                formatted = formattedValue
            )
        }
    }
}