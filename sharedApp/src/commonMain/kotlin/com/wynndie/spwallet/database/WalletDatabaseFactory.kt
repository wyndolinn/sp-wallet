package com.wynndie.spwallet.database

import androidx.room.RoomDatabase

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class WalletDatabaseFactory {
    fun create(): RoomDatabase.Builder<WalletDatabase>
}