package com.wynndie.spwallet.sharedFeature.wallet.domain.model

data class Transfer(
    val receiverCardNumber: String,
    val amount: Long,
    val comment: String
)