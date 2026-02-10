package com.wynndie.spwallet.database

import androidx.room.RoomDatabaseConstructor

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING", "KotlinNoActualForExpect")
expect object WalletDatabaseConstructor : RoomDatabaseConstructor<WalletDatabase> {
    override fun initialize(): WalletDatabase
}