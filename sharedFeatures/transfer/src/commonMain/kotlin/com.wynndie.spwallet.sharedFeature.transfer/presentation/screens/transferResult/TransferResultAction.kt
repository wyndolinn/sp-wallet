package com.wynndie.spwallet.sharedFeature.transfer.presentation.screens.transferResult

sealed interface TransferResultAction {
    data object OnClickComplete : TransferResultAction
    data object OnClickSaveRecipient : TransferResultAction
}