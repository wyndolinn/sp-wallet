package com.wynndie.spwallet.sharedCore.domain.validators.models

data class BalanceValidationValues(
    val value: String,
    val minValue: Long = 1,
    val maxValue: Long = 999_999_999
)
