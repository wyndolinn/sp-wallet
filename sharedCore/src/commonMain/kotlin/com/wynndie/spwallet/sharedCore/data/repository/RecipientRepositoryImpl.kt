package com.wynndie.spwallet.sharedCore.data.repository

import androidx.sqlite.SQLiteException
import com.wynndie.spwallet.sharedCore.data.local.database.RecipientDao
import com.wynndie.spwallet.sharedCore.data.local.model.RecipientEntity
import com.wynndie.spwallet.sharedCore.domain.error.DataError
import com.wynndie.spwallet.sharedCore.domain.error.EmptyOutcome
import com.wynndie.spwallet.sharedCore.domain.error.Outcome
import com.wynndie.spwallet.sharedCore.domain.model.RecipientCard
import com.wynndie.spwallet.sharedCore.domain.repository.RecipientRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RecipientRepositoryImpl(
    private val recipientDao: RecipientDao
) : RecipientRepository {

    override suspend fun insertRecipient(
        recipientCard: RecipientCard
    ): EmptyOutcome<DataError.Local> {
        return try {
            recipientDao.insertRecipient(RecipientEntity.from(recipientCard))
            Outcome.Success(Unit)
        } catch (_: SQLiteException) {
            Outcome.Error(DataError.Local.DISK_FULL)
        }
    }

    override fun getRecipients(): Flow<List<RecipientCard>> {
        return recipientDao
            .getRecipients()
            .map { recipientEntities ->
                recipientEntities.map { it.toRecipient() }
            }
    }

    override suspend fun deleteRecipient(recipientCard: RecipientCard) {
        recipientDao.deleteRecipient(RecipientEntity.from(recipientCard))
    }
}