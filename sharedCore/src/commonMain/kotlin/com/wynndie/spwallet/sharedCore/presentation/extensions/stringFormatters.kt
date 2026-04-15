package com.wynndie.spwallet.sharedCore.presentation.extensions

import androidx.compose.runtime.Composable
import com.wynndie.spwallet.sharedCore.presentation.formatters.DisplayableOreValue

fun String.asFormattedAmount(): String {
    return this.replace(Regex("\\B(?=(\\d{3})+(?!\\d))"), " ")
}

@Composable
fun Long.asDisplayableOre(): DisplayableOreValue {
    return DisplayableOreValue.of(this)
}