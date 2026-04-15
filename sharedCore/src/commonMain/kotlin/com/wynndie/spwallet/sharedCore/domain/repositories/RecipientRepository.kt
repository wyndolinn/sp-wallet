package com.wynndie.spwallet.sharedCore.domain.repositories

import com.wynndie.spwallet.sharedCore.domain.models.cards.RecipientCard
import kotlinx.coroutines.flow.Flow

interface RecipientRepository {

    suspend fun insertRecipient(recipientCard: RecipientCard)
    fun getRecipients(): Flow<List<RecipientCard>>
    suspend fun deleteRecipient(recipientCard: RecipientCard)
}