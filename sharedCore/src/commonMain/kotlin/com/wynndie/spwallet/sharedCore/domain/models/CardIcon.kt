package com.wynndie.spwallet.sharedCore.domain.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.AddCard
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector

enum class CardIcon() {
    CASH,
    BANK_CARD,
    ADD_CARD,
    PERSON;

    companion object {
        fun from(value: Int) = entries.getOrNull(value) ?: BANK_CARD
    }
}