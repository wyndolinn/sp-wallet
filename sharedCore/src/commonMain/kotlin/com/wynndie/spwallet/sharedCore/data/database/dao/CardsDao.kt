package com.wynndie.spwallet.sharedCore.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.wynndie.spwallet.sharedCore.data.database.entities.AuthedCardEntity
import com.wynndie.spwallet.sharedCore.data.database.entities.CustomCardEntity
import com.wynndie.spwallet.sharedCore.data.database.entities.RecipientEntity
import com.wynndie.spwallet.sharedCore.data.database.entities.UnauthedCardEntity
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