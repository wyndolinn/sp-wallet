package com.wynndie.spwallet.navigation

import kotlinx.serialization.Serializable


sealed interface Route {

    @Serializable
    data object HomeNavGraph : Route {

        @Serializable
        data object Home
    }

    @Serializable
    data object TransferNavGraph : Route {

        @Serializable
        data class SearchRecipient(val cardId: String = "")

        @Serializable
        data object EditRecipient

        @Serializable
        data class TransferByCardNumber(val cardId: String)

        @Serializable
        data class TransferBetweenCards(val cardId: String)
    }

    @Serializable
    data object EditNavGraph : Route {

        @Serializable
        data class CustomCard(val cardId: String = "")
    }
}