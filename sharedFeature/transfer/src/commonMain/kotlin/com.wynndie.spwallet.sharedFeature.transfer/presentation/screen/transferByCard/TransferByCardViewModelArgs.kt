package com.wynndie.spwallet.sharedFeature.transfer.presentation.screen.transferByCard

data class TransferByCardViewModelArgs(
    val cardId: String?,
    val onClickBack: () -> Unit,
    val onClickRecipient: () -> Unit
)
