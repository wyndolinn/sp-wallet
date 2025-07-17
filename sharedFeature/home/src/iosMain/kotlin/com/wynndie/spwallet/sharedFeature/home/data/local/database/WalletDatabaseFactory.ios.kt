package com.wynndie.spwallet.sharedFeature.home.data.local.database

import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual class WalletDatabaseFactory {

    actual fun create(): RoomDatabase.Builder<WalletDatabase> {
        val dbFile = documentDirectory() + "/${WalletDatabase.DB_NAME}"
        return Room.databaseBuilder<WalletDatabase>(name = dbFile)
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun documentDirectory(): String {
        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null
        )
        return requireNotNull(documentDirectory?.path)
    }
}