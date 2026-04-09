package com.wynndie.spwallet.sharedCore.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.wynndie.spwallet.sharedCore.data.local.entities.AuthedCardEntity
import com.wynndie.spwallet.sharedCore.data.local.entities.CustomCardEntity
import com.wynndie.spwallet.sharedCore.data.local.entities.RecipientEntity
import com.wynndie.spwallet.sharedCore.data.local.entities.UnauthedCardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CardsDao {

    @Upsert
    suspend fun insertCustomCard(card: CustomCardEntity)

    @Query("SELECT * FROM customcardentity")
    fun getCustomCards(): Flow<List<CustomCardEntity>>

    @Delete
    suspend fun deleteCustomCard(card: CustomCardEntity)


    @Upsert
    suspend fun insertAuthedCard(card: AuthedCardEntity)

    @Query("SELECT * FROM authedcardentity")
    fun getAuthedCards(): Flow<List<AuthedCardEntity>>

    @Delete
    suspend fun deleteAuthedCard(card: AuthedCardEntity)


    @Upsert
    suspend fun insertUnauthedCard(card: UnauthedCardEntity)

    @Query("SELECT * FROM unauthedcardentity")
    fun getUnauthedCards(): Flow<List<UnauthedCardEntity>>

    @Delete
    suspend fun deleteUnauthedCard(card: UnauthedCardEntity)


    @Upsert
    suspend fun insertRecipient(recipient: RecipientEntity)

    @Query("SELECT * FROM recipiententity")
    fun getRecipients(): Flow<List<RecipientEntity>>

    @Delete
    suspend fun deleteRecipient(recipient: RecipientEntity)
}