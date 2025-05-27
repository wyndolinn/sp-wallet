package com.wynndie.spwallet.sharedFeature.wallet.data.local.database

import androidx.room.RoomDatabase

expect class WalletDatabaseFactory {
    fun create(): RoomDatabase.Builder<WalletDatabase>
}