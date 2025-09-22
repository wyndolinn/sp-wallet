package com.wynndie.spwallet.sharedFeature.transfer.domain.models

data class Transfer(
    val receiverCardNumber: String,
    val amount: Long,
    val comment: String
)