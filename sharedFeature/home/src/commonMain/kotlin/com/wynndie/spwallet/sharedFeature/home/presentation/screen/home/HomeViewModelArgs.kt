package com.wynndie.spwallet.sharedFeature.home.presentation.screen.home

data class HomeViewModelArgs(
    val onClickTopAppBar: () -> Unit,
    val onClickCashCard: (cardId: String?) -> Unit,
    val onClickTransferByCard: (cardId: String?) -> Unit
)
