package com.wynndie.spwallet.sharedCore.presentation.formatters.displayableValue

import com.wynndie.spwallet.sharedCore.presentation.formatters.UiText
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.x_of_ore
import com.wynndie.spwallet.sharedResources.x_of_shulkers
import com.wynndie.spwallet.sharedResources.x_of_stacks
import kotlinx.serialization.Serializable

data class OreDisplayableValue(
    override val value: Long,
    override val formatted: List<UiText>
) : DisplayableValue<List<UiText>> {

    companion object {
        private const val ORE_IN_STACK = 64
        private const val STACKS_IN_SHULKER = 27

        fun of(value: Long): OreDisplayableValue {
            if (value == 0L) {
                return OreDisplayableValue(
                    value = value,
                    formatted = listOf(UiText.ResourceString(Res.string.x_of_ore, value))
                )
            }

            val shulkers = value / ORE_IN_STACK / STACKS_IN_SHULKER
            val stacks = value / ORE_IN_STACK % STACKS_IN_SHULKER
            val ore = value % ORE_IN_STACK

            val formattedValue = buildList {
                if (shulkers > 0) add(UiText.ResourceString(Res.string.x_of_shulkers, shulkers))
                if (stacks > 0) add(UiText.ResourceString(Res.string.x_of_stacks, stacks))
                if (ore > 0) add(UiText.ResourceString(Res.string.x_of_ore, ore))
            }

            return OreDisplayableValue(
                value = value,
                formatted = formattedValue
            )
        }
    }
}