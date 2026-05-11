package com.wynndie.spwallet.sharedCore.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import com.wynndie.spwallet.sharedCore.data.database.dao.CardsDao
import com.wynndie.spwallet.sharedCore.data.database.dao.RecipientDao
import com.wynndie.spwallet.sharedCore.data.database.dao.UserDao
import com.wynndie.spwallet.sharedCore.data.database.entities.AuthedCardEntity
import com.wynndie.spwallet.sharedCore.data.database.entities.AuthedUserEntity
import com.wynndie.spwallet.sharedCore.data.database.entities.CustomCardEntity
import com.wynndie.spwallet.sharedCore.data.database.entities.RecipientEntity
import com.wynndie.spwallet.sharedCore.data.database.entities.UnauthedCardEntity

@Database(
    entities = [
        AuthedUserEntity::class,
        CustomCardEntity::class,
        AuthedCardEntity::class,
        UnauthedCardEntity::class,
        RecipientEntity::class
    ],
    version = 1
)
@ConstructedBy(WalletDatabaseConstructor::class)
abstract class WalletDatabase : RoomDatabase() {

    abstract val userDao: UserDao
    abstract val cardsDao: CardsDao
    abstract val recipientDao: RecipientDao

    companion object {
        const val DB_NAME = "wallet.db"
    }
}