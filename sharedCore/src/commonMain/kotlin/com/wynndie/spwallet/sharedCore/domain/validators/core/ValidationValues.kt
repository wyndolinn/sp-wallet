package com.wynndie.spwallet.sharedCore.domain.validators.core

data class ValidationValues(
    val value: String,
    val minValue: Long = Long.MIN_VALUE,
    val maxValue: Long = Long.MAX_VALUE,
    val exactValue: Long = 0L,
    val minLength: Int = Int.MIN_VALUE,
    val maxLength: Int = Int.MAX_VALUE,
    val exactLength: Int = 0,
)
