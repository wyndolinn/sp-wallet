package com.wynndie.spwallet.sharedCore.data.repository

import androidx.sqlite.SQLiteException
import com.wynndie.spwallet.sharedCore.data.local.database.CardsDao
import com.wynndie.spwallet.sharedCore.data.local.model.AuthedCardEntity
import com.wynndie.spwallet.sharedCore.data.local.model.CashCardEntity
import com.wynndie.spwallet.sharedCore.data.local.model.UnauthedCardEntity
import com.wynndie.spwallet.sharedCore.data.remote.network.RemoteSpWorldsCardsDataSource
import com.wynndie.spwallet.sharedCore.domain.error.DataError
import com.wynndie.spwallet.sharedCore.domain.error.EmptyOutcome
import com.wynndie.spwallet.sharedCore.domain.error.Outcome
import com.wynndie.spwallet.sharedCore.domain.error.map
import com.wynndie.spwallet.sharedCore.domain.model.AuthedCard
import com.wynndie.spwallet.sharedCore.domain.model.CardBalance
import com.wynndie.spwallet.sharedCore.domain.model.CashCard
import com.wynndie.spwallet.sharedCore.domain.model.UnauthedCard
import com.wynndie.spwallet.sharedCore.domain.repository.CardsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CardsRepositoryImpl(
    private val remoteSpWorldsCardsDataSource: RemoteSpWorldsCardsDataSource,
    private val cardsDao: CardsDao
) : CardsRepository {

    override suspend fun getCardBalance(
        authKey: String
    ): Outcome<CardBalance, DataError.Remote> {
        return remoteSpWorldsCardsDataSource
            .getCardBalance(authKey = authKey)
            .map { it.toCardBalance() }
    }


    override suspend fun insertCashCard(
        card: CashCard
    ): EmptyOutcome<DataError.Local> {
        return try {
            cardsDao.insertCashCard(CashCardEntity.from(card))
            Outcome.Success(Unit)
        } catch (_: SQLiteException) {
            Outcome.Error(DataError.Local.DISK_FULL)
        }
    }

    override fun getCashCards(): Flow<List<CashCard>> {
        return cardsDao
            .getCashCards()
            .map { cashCardEntities ->
                cashCardEntities.map { it.toCashCard() }
            }
    }

    override suspend fun deleteCashCard(card: CashCard) {
        cardsDao.deleteCashCard(CashCardEntity.from(card))
    }


    override suspend fun insertAuthedCard(
        card: AuthedCard
    ): EmptyOutcome<DataError.Local> {
        return try {
            cardsDao.insertAuthedCard(AuthedCardEntity.from(card))
            Outcome.Success(Unit)
        } catch (_: SQLiteException) {
            Outcome.Error(DataError.Local.DISK_FULL)
        }
    }

    override fun getAuthedCards(): Flow<List<AuthedCard>> {
        return cardsDao
            .getAuthedCards()
            .map { authedCardEntities ->
                authedCardEntities.map { it.toAuthedCard() }
            }
    }

    override suspend fun deleteAuthedCard(card: AuthedCard) {
        cardsDao.deleteAuthedCard(AuthedCardEntity.from(card))
    }


    override suspend fun insertUnauthedCard(
        card: UnauthedCard
    ): EmptyOutcome<DataError.Local> {
        return try {
            cardsDao.insertUnauthedCard(UnauthedCardEntity.from(card))
            Outcome.Success(Unit)
        } catch (_: SQLiteException) {
            Outcome.Error(DataError.Local.DISK_FULL)
        }
    }

    override fun getUnauthedCards(): Flow<List<UnauthedCard>> {
        return cardsDao
            .getUnauthedCards()
            .map { unAuthedCardEntities ->
                unAuthedCardEntities.map { it.toUnauthedCard() }
            }
    }

    override suspend fun deleteUnauthedCard(card: UnauthedCard) {
        cardsDao.deleteUnauthedCard(UnauthedCardEntity.from(card))
    }
}