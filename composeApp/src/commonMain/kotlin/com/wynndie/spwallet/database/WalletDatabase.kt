package com.wynndie.spwallet.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wynndie.spwallet.sharedCore.data.local.database.WalletDao
import com.wynndie.spwallet.sharedCore.data.local.model.AuthedCardEntity
import com.wynndie.spwallet.sharedCore.data.local.model.AuthedUserEntity
import com.wynndie.spwallet.sharedCore.data.local.model.CashCardEntity
import com.wynndie.spwallet.sharedCore.data.local.model.RecipientEntity
import com.wynndie.spwallet.sharedCore.data.local.model.UnauthedCardEntity

@Database(
    entities = [
        AuthedUserEntity::class,
        CashCardEntity::class,
        AuthedCardEntity::class,
        UnauthedCardEntity::class,
        RecipientEntity::class
    ],
    version = 1
)
abstract class WalletDatabase : RoomDatabase() {
    abstract val walletDao: WalletDao

    companion object {
        const val DB_NAME = "wallet.db"
    }
}