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
interface RecipientDao {

    @Upsert
    suspend fun insertRecipient(recipient: RecipientEntity)

    @Query("SELECT * FROM recipiententity")
    fun getRecipients(): Flow<List<RecipientEntity>>

    @Delete
    suspend fun deleteRecipient(recipient: RecipientEntity)
}