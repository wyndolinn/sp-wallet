package com.wynndie.spwallet.sharedFeature.wallet.data.local.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

actual class WalletDatabaseFactory(
    private val context: Context
) {

    actual fun create(): RoomDatabase.Builder<WalletDatabase> {
        val appContext = context.applicationContext
        val dbFile = appContext.getDatabasePath(WalletDatabase.DB_NAME)

        return Room.databaseBuilder(
            context = appContext,
            name = dbFile.absolutePath
        )
    }
}