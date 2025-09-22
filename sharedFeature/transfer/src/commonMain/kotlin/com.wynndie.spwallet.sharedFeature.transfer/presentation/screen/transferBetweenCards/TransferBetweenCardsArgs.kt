package com.wynndie.spwallet.sharedFeature.transfer.presentation.screen.transferBetweenCards

data class TransferBetweenCardsArgs(
    val destinationCardId: String?,
    val onClickBack: () -> Unit
)
