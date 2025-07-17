package com.wynndie.spwallet.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class WalletDatabaseFactory(
    private val context: Context
) {

    actual fun create(): RoomDatabase.Builder<WalletDatabase> {
        val appContext = context.applicationContext
        val dbFile = appContext.getDatabasePath(WalletDatabase.Companion.DB_NAME)

        return Room.databaseBuilder(
            context = appContext,
            name = dbFile.absolutePath
        )
    }
}