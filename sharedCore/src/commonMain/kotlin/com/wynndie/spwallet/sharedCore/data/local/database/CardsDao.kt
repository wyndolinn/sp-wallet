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
interface CardsDao {

    @Upsert
    suspend fun insertCashCard(card: CashCardEntity)

    @Query("SELECT * FROM cashcardentity")
    fun getCashCards(): Flow<List<CashCardEntity>>

    @Delete
    suspend fun deleteCashCard(card: CashCardEntity)


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