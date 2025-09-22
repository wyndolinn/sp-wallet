package com.wynndie.spwallet.sharedCore.domain.repositories

import com.wynndie.spwallet.sharedCore.domain.error.DataError
import com.wynndie.spwallet.sharedCore.domain.error.EmptyOutcome
import com.wynndie.spwallet.sharedCore.domain.models.RecipientCard
import kotlinx.coroutines.flow.Flow

interface RecipientRepository {

    suspend fun insertRecipient(recipientCard: RecipientCard): EmptyOutcome<DataError.Local>
    fun getRecipients(): Flow<List<RecipientCard>>
    suspend fun deleteRecipient(recipientCard: RecipientCard)
}