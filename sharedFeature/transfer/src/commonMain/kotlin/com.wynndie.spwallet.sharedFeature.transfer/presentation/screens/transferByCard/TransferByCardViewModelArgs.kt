package com.wynndie.spwallet.sharedFeature.transfer.presentation.screens.transferByCard

data class TransferByCardViewModelArgs(
    val cardId: String?,
    val onClickBack: () -> Unit,
    val onClickRecipient: () -> Unit,
    val onClickEditRecipient: (String) -> Unit
)
