package com.wynndie.spwallet.sharedCore.presentation.formatters

import androidx.compose.runtime.Composable
import com.wynndie.spwallet.sharedCore.presentation.formatters.displayableValue.OreDisplayableValue

fun String.formatAsAmount(): String {
    return this.replace(Regex("\\B(?=(\\d{3})+(?!\\d))"), " ")
}

@Composable
fun Long.formatAsDisplayableOre(): OreDisplayableValue {
    return  OreDisplayableValue.of(this)
}

@Composable
fun List<UiText>.joinToString(separator: String): String {
    @Suppress("SimplifiableCallChain")
    return UiText.DynamicString(this.map { it.asString() }.joinToString(separator)).asString()
}