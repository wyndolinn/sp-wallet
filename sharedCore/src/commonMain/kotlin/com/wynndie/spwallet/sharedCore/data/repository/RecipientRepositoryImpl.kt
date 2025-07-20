package com.wynndie.spwallet.sharedCore.data.repository

import androidx.sqlite.SQLiteException
import com.wynndie.spwallet.sharedCore.data.local.database.RecipientDao
import com.wynndie.spwallet.sharedCore.domain.error.DataError
import com.wynndie.spwallet.sharedCore.domain.error.EmptyOutcome
import com.wynndie.spwallet.sharedCore.domain.error.Outcome
import com.wynndie.spwallet.sharedCore.domain.error.map
import com.wynndie.spwallet.sharedCore.data.local.database.WalletDao
import com.wynndie.spwallet.sharedCore.data.local.model.AuthedCardEntity
import com.wynndie.spwallet.sharedCore.data.local.model.AuthedUserEntity
import com.wynndie.spwallet.sharedCore.data.local.model.CashCardEntity
import com.wynndie.spwallet.sharedCore.data.local.model.RecipientEntity
import com.wynndie.spwallet.sharedCore.data.local.model.UnauthedCardEntity
import com.wynndie.spwallet.sharedFeature.home.data.remote.model.TransferDto
import com.wynndie.spwallet.sharedFeature.home.data.remote.network.RemoteSpWorldsDataSource
import com.wynndie.spwallet.sharedCore.domain.model.AuthedUser
import com.wynndie.spwallet.sharedFeature.home.domain.model.Transfer
import com.wynndie.spwallet.sharedCore.domain.model.UnauthedUser
import com.wynndie.spwallet.sharedCore.domain.model.AuthedCard
import com.wynndie.spwallet.sharedCore.domain.model.CardBalance
import com.wynndie.spwallet.sharedCore.domain.model.CashCard
import com.wynndie.spwallet.sharedCore.domain.model.RecipientCard
import com.wynndie.spwallet.sharedCore.domain.model.UnauthedCard
import com.wynndie.spwallet.sharedCore.domain.repository.RecipientRepository
import com.wynndie.spwallet.sharedFeature.home.domain.repository.WalletRepository
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