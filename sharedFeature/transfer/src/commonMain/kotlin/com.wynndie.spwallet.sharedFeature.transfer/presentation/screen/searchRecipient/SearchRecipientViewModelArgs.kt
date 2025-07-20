package com.wynndie.spwallet.sharedFeature.transfer.presentation.screen.searchRecipient

data class SearchRecipientViewModelArgs(
    val onClickBack: () -> Unit,
    val onClickRecipient: (id: String?, cardNumber: String?) -> Unit,
    val onClickEditRecipient: (id: String) -> Unit
)
