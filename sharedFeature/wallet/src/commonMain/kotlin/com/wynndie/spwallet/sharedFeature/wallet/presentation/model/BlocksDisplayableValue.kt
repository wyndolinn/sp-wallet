package com.wynndie.spwallet.sharedFeature.wallet.presentation.model

import com.wynndie.spwallet.sharedCore.presentation.model.DisplayableNumber
import com.wynndie.spwallet.sharedCore.presentation.model.UiText
import com.wynndie.spwallet.sharedResources.Res
import com.wynndie.spwallet.sharedResources.amount_of_ore
import com.wynndie.spwallet.sharedResources.amount_of_shulkers
import com.wynndie.spwallet.sharedResources.amount_of_stacks

data class BlocksDisplayableValue(
    override val value: Long,
    override val formatted: List<UiText>
) : DisplayableNumber<List<UiText>> {

    companion object {
        private const val ORE_IN_STACK = 64
        private const val STACKS_IN_SHULKER = 27

        fun of(value: Long): BlocksDisplayableValue {
            if (value == 0L) {
                return BlocksDisplayableValue(
                    value = value,
                    formatted = listOf(UiText.StringResourceId(Res.string.amount_of_ore, value))
                )
            }

            val shulkers = value / ORE_IN_STACK / STACKS_IN_SHULKER
            val stacks = value / ORE_IN_STACK % STACKS_IN_SHULKER
            val ore = value % ORE_IN_STACK

            val formattedValue =  buildList {
                if (shulkers > 0) add(UiText.StringResourceId(Res.string.amount_of_shulkers, shulkers))
                if (stacks > 0) add(UiText.StringResourceId(Res.string.amount_of_stacks, stacks))
                if (ore > 0) add(UiText.StringResourceId(Res.string.amount_of_ore, ore))
            }

            return BlocksDisplayableValue(
                value = value,
                formatted = formattedValue
            )
        }
    }
}