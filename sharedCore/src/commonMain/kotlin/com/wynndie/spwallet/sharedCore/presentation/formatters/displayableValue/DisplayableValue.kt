package com.wynndie.spwallet.sharedCore.presentation.formatters.displayableValue

interface DisplayableValue<T> {
    val value: Number
    val formatted: T
}