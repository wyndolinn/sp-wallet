package com.wynndie.spwallet.sharedCore.presentation.formatters

import androidx.compose.runtime.Composable

fun String.asFormattedAmount(): String {
    return this.replace(Regex("\\B(?=(\\d{3})+(?!\\d))"), " ")
}

@Composable
fun Long.asDisplayableOre(): DisplayableOreValue {
    return  DisplayableOreValue.of(this)
}