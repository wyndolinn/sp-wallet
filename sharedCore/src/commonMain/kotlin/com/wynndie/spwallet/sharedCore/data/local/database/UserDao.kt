package com.wynndie.spwallet.sharedCore.data.local.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.wynndie.spwallet.sharedCore.data.local.model.AuthedCardEntity
import com.wynndie.spwallet.sharedCore.data.local.model.AuthedUserEntity
import com.wynndie.spwallet.sharedCore.data.local.model.CashCardEntity
import com.wynndie.spwallet.sharedCore.data.local.model.RecipientEntity
import com.wynndie.spwallet.sharedCore.data.local.model.UnauthedCardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Upsert
    suspend fun insertAuthedUser(user: AuthedUserEntity)

    @Query("SELECT * FROM autheduserentity")
    fun getAuthedUsers(): Flow<List<AuthedUserEntity>>

    @Delete
    suspend fun deleteAuthedUser(user: AuthedUserEntity)
}