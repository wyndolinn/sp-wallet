package com.wynndie.spwallet.sharedFeature.wallet.data.local.database

import androidx.room.RoomDatabaseConstructor

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object WalletDatabaseConstructor : RoomDatabaseConstructor<WalletDatabase> {

    override fun initialize(): WalletDatabase
}