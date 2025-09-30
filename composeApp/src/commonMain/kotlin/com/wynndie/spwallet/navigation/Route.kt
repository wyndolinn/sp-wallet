package com.wynndie.spwallet.navigation

import kotlinx.serialization.Serializable


sealed interface Route


sealed interface HomeNavGraphRoutes : Route {

    @Serializable
    data object HomeNavGraph : HomeNavGraphRoutes

    @Serializable
    data object Home : HomeNavGraphRoutes
}


sealed interface TransferNavGraphRoutes : Route {

    @Serializable
    data object TransferNavGraph : TransferNavGraphRoutes

    @Serializable
    data class SearchRecipient(
        val cardId: String? = null
    ) : TransferNavGraphRoutes

    @Serializable
    data object EditSearchRecipient : TransferNavGraphRoutes

    @Serializable
    data class TransferByCardNumber(
        val cardId: String?
    ) : TransferNavGraphRoutes

    @Serializable
    data class TransferBetweenCards(
        val cardId: String?
    ) : TransferNavGraphRoutes

    @Serializable
    data object TransferResult : TransferNavGraphRoutes
}

sealed interface EditNavGraphRoutes : Route {

    @Serializable
    data object EditNavGraph : TransferNavGraphRoutes

    @Serializable
    data class CustomCard(
        val cardId: String? = null
    ) : TransferNavGraphRoutes

    @Serializable
    data object EditResult : TransferNavGraphRoutes
}