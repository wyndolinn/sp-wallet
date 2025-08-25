package com.wynndie.spwallet.sharedCore.presentation.model.displayableValue

interface DisplayableNumber<T> {
    val value: Number
    val formatted: T
}