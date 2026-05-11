package com.wynndie.spwallet.sharedCore.data.repositories

import com.wynndie.spwallet.sharedCore.data.database.WalletDatabase
import com.wynndie.spwallet.sharedCore.data.mappers.toDomain
import com.wynndie.spwallet.sharedCore.data.mappers.toEntity
import com.wynndie.spwallet.sharedCore.domain.models.cards.RecipientCard
import com.wynndie.spwallet.sharedCore.domain.repositories.RecipientRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RecipientRepositoryImpl(
    private val database: WalletDatabase
) : RecipientRepository {

    override suspend fun insertRecipient(
        recipientCard: RecipientCard
    ) {
        database.recipientDao.insertRecipient(recipientCard.toEntity())
    }

    override fun getRecipients(): Flow<List<RecipientCard>> {
        return database.recipientDao.getRecipients().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun deleteRecipient(recipientCard: RecipientCard) {
        database.recipientDao.deleteRecipient(recipientCard.toEntity())
    }
}