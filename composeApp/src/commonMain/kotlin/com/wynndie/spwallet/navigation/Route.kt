package com.wynndie.spwallet.navigation

import kotlinx.serialization.Serializable


sealed interface Route


sealed interface HomeNavGraphRoutes : Route {

    @Serializable
    data object HomeNavGraph : HomeNavGraphRoutes

    @Serializable
    data object Home : HomeNavGraphRoutes

    @Serializable
    data class CashCard(val cardId: String? = null) : HomeNavGraphRoutes

    @Serializable
    data class TransferByCard(val cardId: String? = null) : HomeNavGraphRoutes
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
    data object TransferBetweenCards : TransferNavGraphRoutes

    @Serializable
    data object TransferResult : TransferNavGraphRoutes
}

sealed interface ProfileNavGraphRoutes : Route {

    @Serializable
    data object ProfileNavGraph : ProfileNavGraphRoutes

    @Serializable
    data object Menu : ProfileNavGraphRoutes

    @Serializable
    data object Theme : ProfileNavGraphRoutes
}