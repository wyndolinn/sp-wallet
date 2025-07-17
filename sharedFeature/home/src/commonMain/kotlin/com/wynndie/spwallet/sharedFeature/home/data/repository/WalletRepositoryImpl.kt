package com.wynndie.spwallet.sharedFeature.home.data.repository

import androidx.sqlite.SQLiteException
import com.wynndie.spwallet.sharedCore.domain.error.DataError
import com.wynndie.spwallet.sharedCore.domain.error.EmptyOutcome
import com.wynndie.spwallet.sharedCore.domain.error.Outcome
import com.wynndie.spwallet.sharedCore.domain.error.map
import com.wynndie.spwallet.sharedFeature.home.data.local.database.WalletDao
import com.wynndie.spwallet.sharedFeature.home.data.local.model.AuthedCardEntity
import com.wynndie.spwallet.sharedFeature.home.data.local.model.AuthedUserEntity
import com.wynndie.spwallet.sharedFeature.home.data.local.model.CashCardEntity
import com.wynndie.spwallet.sharedFeature.home.data.local.model.RecipientEntity
import com.wynndie.spwallet.sharedFeature.home.data.local.model.UnauthedCardEntity
import com.wynndie.spwallet.sharedFeature.home.data.remote.model.TransferDto
import com.wynndie.spwallet.sharedFeature.home.data.remote.network.RemoteSpWorldsDataSource
import com.wynndie.spwallet.sharedFeature.home.domain.model.AuthedUser
import com.wynndie.spwallet.sharedFeature.home.domain.model.Transfer
import com.wynndie.spwallet.sharedFeature.home.domain.model.UnauthedUser
import com.wynndie.spwallet.sharedFeature.home.domain.model.card.AuthedCard
import com.wynndie.spwallet.sharedFeature.home.domain.model.card.CardBalance
import com.wynndie.spwallet.sharedFeature.home.domain.model.card.CashCard
import com.wynndie.spwallet.sharedFeature.home.domain.model.card.RecipientCard
import com.wynndie.spwallet.sharedFeature.home.domain.model.card.UnauthedCard
import com.wynndie.spwallet.sharedFeature.home.domain.repository.WalletRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WalletRepositoryImpl(
    private val remoteSpWorldsDataSource: RemoteSpWorldsDataSource,
    private val walletDao: WalletDao
) : WalletRepository {

    override suspend fun getUnauthedUser(
        authKey: String
    ): Outcome<UnauthedUser, DataError.Remote> {
        return remoteSpWorldsDataSource
            .getUnauthedUser(authKey = authKey)
            .map { it.toUnauthedUser() }
    }

    override suspend fun getCardBalance(
        authKey: String
    ): Outcome<CardBalance, DataError.Remote> {
        return remoteSpWorldsDataSource
            .getCardBalance(authKey = authKey)
            .map { it.toCardBalance() }
    }

    override suspend fun makeTransaction(
        authKey: String,
        transfer: Transfer
    ): Outcome<CardBalance, DataError.Remote> {
        return remoteSpWorldsDataSource
            .makeTransaction(
                authKey = authKey,
                transaction = TransferDto.from(transfer)
            ).map { it.toCardBalance() }
    }


    override suspend fun insertAuthedUser(
        user: AuthedUser
    ): EmptyOutcome<DataError.Local> {
        return try {
            walletDao.insertAuthedUser(AuthedUserEntity.from(user))
            Outcome.Success(Unit)
        } catch (_: SQLiteException) {
            Outcome.Error(DataError.Local.DISK_FULL)
        }
    }

    override fun getAuthedUsers(): Flow<List<AuthedUser>> {
        return walletDao
            .getAuthedUsers()
            .map { authedUserEntities ->
                authedUserEntities.map { it.toAuthedUser() }
            }
    }

    override suspend fun deleteAuthedUser(user: AuthedUser) {
        walletDao.deleteAuthedUser(AuthedUserEntity.from(user))
    }


    override suspend fun insertCashCard(
        card: CashCard
    ): EmptyOutcome<DataError.Local> {
        return try {
            walletDao.insertCashCard(CashCardEntity.from(card))
            Outcome.Success(Unit)
        } catch (_: SQLiteException) {
            Outcome.Error(DataError.Local.DISK_FULL)
        }
    }

    override fun getCashCards(): Flow<List<CashCard>> {
        return walletDao
            .getCashCards()
            .map { cashCardEntities ->
                cashCardEntities.map { it.toCashCard() }
            }
    }

    override suspend fun deleteCashCard(card: CashCard) {
        walletDao.deleteCashCard(CashCardEntity.from(card))
    }


    override suspend fun insertAuthedCard(
        card: AuthedCard
    ): EmptyOutcome<DataError.Local> {
        return try {
            walletDao.insertAuthedCard(AuthedCardEntity.from(card))
            Outcome.Success(Unit)
        } catch (_: SQLiteException) {
            Outcome.Error(DataError.Local.DISK_FULL)
        }
    }

    override fun getAuthedCards(): Flow<List<AuthedCard>> {
        return walletDao
            .getAuthedCards()
            .map { authedCardEntities ->
                authedCardEntities.map { it.toAuthedCard() }
            }
    }

    override suspend fun deleteAuthedCard(card: AuthedCard) {
        walletDao.deleteAuthedCard(AuthedCardEntity.from(card))
    }


    override suspend fun insertUnauthedCard(
        card: UnauthedCard
    ): EmptyOutcome<DataError.Local> {
        return try {
            walletDao.insertUnauthedCard(UnauthedCardEntity.from(card))
            Outcome.Success(Unit)
        } catch (_: SQLiteException) {
            Outcome.Error(DataError.Local.DISK_FULL)
        }
    }

    override fun getUnauthedCards(): Flow<List<UnauthedCard>> {
        return walletDao
            .getUnauthedCards()
            .map { unAuthedCardEntities ->
                unAuthedCardEntities.map { it.toUnauthedCard() }
            }
    }

    override suspend fun deleteUnauthedCard(card: UnauthedCard) {
        walletDao.deleteUnauthedCard(UnauthedCardEntity.from(card))
    }


    override suspend fun insertRecipient(
        recipientCard: RecipientCard
    ): EmptyOutcome<DataError.Local> {
        return try {
            walletDao.insertRecipient(RecipientEntity.from(recipientCard))
            Outcome.Success(Unit)
        } catch (_: SQLiteException) {
            Outcome.Error(DataError.Local.DISK_FULL)
        }
    }

    override fun getRecipients(): Flow<List<RecipientCard>> {
        return walletDao
            .getRecipients()
            .map { recipientEntities ->
                recipientEntities.map { it.toRecipient() }
            }
    }

    override suspend fun deleteRecipient(recipientCard: RecipientCard) {
        walletDao.deleteRecipient(RecipientEntity.from(recipientCard))
    }
}