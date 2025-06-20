package com.wynndie.spwallet.sharedFeature.wallet.data.local.database

import androidx.room.RoomDatabaseConstructor

@Suppress("NO_ACTUAL_FOR_EXPECT", "EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object WalletDatabaseConstructor : RoomDatabaseConstructor<WalletDatabase> {

    override fun initialize(): WalletDatabase
}