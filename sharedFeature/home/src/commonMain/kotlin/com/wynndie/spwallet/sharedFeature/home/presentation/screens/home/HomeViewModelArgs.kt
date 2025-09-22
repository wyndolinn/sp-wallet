package com.wynndie.spwallet.sharedFeature.home.presentation.screens.home

data class HomeViewModelArgs(
    val onClickCustomCard: (cardId: String?) -> Unit,
    val onClickTransferByCard: (cardId: String?) -> Unit
)
