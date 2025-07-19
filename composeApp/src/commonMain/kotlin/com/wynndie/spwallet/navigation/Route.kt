package com.wynndie.spwallet.navigation

import kotlinx.serialization.Serializable

sealed interface Route

sealed interface WalletNavGraph : Route {

    @Serializable
    data object RootGraph : WalletNavGraph

    @Serializable
    data object Home : WalletNavGraph

    @Serializable
    data class CashCard(val cardId: String?) : WalletNavGraph

    @Serializable
    data class TransferByCard(val cardId: String?) : WalletNavGraph
}