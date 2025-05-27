package com.wynndie.spwallet.sharedFeature.wallet.presentation.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.AddCard
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector

enum class CardIcon(val value: ImageVector) {
    Cash(Icons.Outlined.AccountBalanceWallet),
    BankCard(Icons.Outlined.CreditCard),
    AddCard(Icons.Outlined.AddCard),
    Person(Icons.Outlined.Person);

    companion object {
        fun from(value: Int) = entries.getOrNull(value) ?: Cash
    }
}