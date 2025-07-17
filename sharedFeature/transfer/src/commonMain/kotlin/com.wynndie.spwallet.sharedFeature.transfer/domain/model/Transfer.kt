package com.wynndie.spwallet.sharedFeature.transfer.domain.model

data class Transfer(
    val receiverCardNumber: String,
    val amount: Long,
    val comment: String
)