package com.wynndie.spwallet.sharedFeature.home.presentation.screen.home

data class HomeViewModelArgs(
    val onClickCashCard: (cardId: String?) -> Unit,
    val onClickTransferByCard: (cardId: String?) -> Unit
)
