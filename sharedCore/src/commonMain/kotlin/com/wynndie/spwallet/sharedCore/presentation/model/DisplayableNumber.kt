package com.wynndie.spwallet.sharedCore.presentation.model

interface DisplayableNumber<T> {
    val value: Number
    val formatted: T
}