package com.wynndie.spwallet.sharedCore.presentation.formatters

internal fun String.formatAsAmount(): String {
    return this.replace(Regex("\\B(?=(\\d{3})+(?!\\d))"), " ")
}