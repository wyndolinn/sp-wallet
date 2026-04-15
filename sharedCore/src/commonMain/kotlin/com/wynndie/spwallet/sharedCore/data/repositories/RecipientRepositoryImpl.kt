package com.wynndie.spwallet.sharedCore.data.repositories

import com.wynndie.spwallet.sharedCore.data.local.dao.RecipientDao
import com.wynndie.spwallet.sharedCore.data.local.entities.RecipientEntity
import com.wynndie.spwallet.sharedCore.domain.models.cards.RecipientCard
import com.wynndie.spwallet.sharedCore.domain.repositories.RecipientRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RecipientRepositoryImpl(
    private val recipientDao: RecipientDao
) : RecipientRepository {

    override suspend fun insertRecipient(
        recipientCard: RecipientCard
    ) {
        recipientDao.insertRecipient(RecipientEntity.of(recipientCard))
    }

    override fun getRecipients(): Flow<List<RecipientCard>> {
        return recipientDao
            .getRecipients()
            .map { recipientEntities ->
                recipientEntities.map { it.toDomain() }
            }
    }

    override suspend fun deleteRecipient(recipientCard: RecipientCard) {
        recipientDao.deleteRecipient(RecipientEntity.of(recipientCard))
    }
}