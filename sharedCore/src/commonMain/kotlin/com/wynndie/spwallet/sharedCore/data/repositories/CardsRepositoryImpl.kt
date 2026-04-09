package com.wynndie.spwallet.sharedCore.data.repositories

import androidx.sqlite.SQLiteException
import com.wynndie.spwallet.sharedCore.data.local.dao.CardsDao
import com.wynndie.spwallet.sharedCore.data.local.entities.AuthedCardEntity
import com.wynndie.spwallet.sharedCore.data.local.entities.CustomCardEntity
import com.wynndie.spwallet.sharedCore.data.local.entities.UnauthedCardEntity
import com.wynndie.spwallet.sharedCore.data.remote.network.RemoteSpWorldsCardsDataSource
import com.wynndie.spwallet.sharedCore.domain.error.DataError
import com.wynndie.spwallet.sharedCore.domain.error.EmptyOutcome
import com.wynndie.spwallet.sharedCore.domain.error.Outcome
import com.wynndie.spwallet.sharedCore.domain.error.map
import com.wynndie.spwallet.sharedCore.domain.models.cards.AuthedCard
import com.wynndie.spwallet.sharedCore.domain.models.cards.CustomCard
import com.wynndie.spwallet.sharedCore.domain.models.cards.UnauthedCard
import com.wynndie.spwallet.sharedCore.domain.repositories.CardsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CardsRepositoryImpl(
    private val remoteSpWorldsCardsDataSource: RemoteSpWorldsCardsDataSource,
    private val cardsDao: CardsDao
) : CardsRepository {

    override suspend fun getCardBalance(
        authKey: String
    ): Outcome<Long, DataError.Remote> {
        return remoteSpWorldsCardsDataSource
            .getCardBalance(authKey = authKey)
            .map { it.balance }
    }


    override suspend fun insertCustomCard(
        card: CustomCard
    ): EmptyOutcome<DataError.Local> {
        return try {
            cardsDao.insertCustomCard(CustomCardEntity.of(card))
            Outcome.Success(Unit)
        } catch (_: SQLiteException) {
            Outcome.Error(DataError.Local.DISK_FULL)
        }
    }

    override fun getCustomCards(): Flow<List<CustomCard>> {
        return cardsDao
            .getCustomCards()
            .map { cashCardEntities ->
                cashCardEntities.map { it.toDomain() }
            }
    }

    override suspend fun deleteCustomCard(card: CustomCard) {
        cardsDao.deleteCustomCard(CustomCardEntity.of(card))
    }


    override suspend fun insertAuthedCard(
        card: AuthedCard
    ): EmptyOutcome<DataError.Local> {
        return try {
            cardsDao.insertAuthedCard(AuthedCardEntity.of(card))
            Outcome.Success(Unit)
        } catch (_: SQLiteException) {
            Outcome.Error(DataError.Local.DISK_FULL)
        }
    }

    override fun getAuthedCards(): Flow<List<AuthedCard>> {
        return cardsDao
            .getAuthedCards()
            .map { authedCardEntities ->
                authedCardEntities.map { it.toDomain() }
            }
    }

    override suspend fun deleteAuthedCard(card: AuthedCard) {
        cardsDao.deleteAuthedCard(AuthedCardEntity.of(card))
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
                unAuthedCardEntities.map { it.toDomain() }
            }
    }

    override suspend fun deleteUnauthedCard(card: UnauthedCard) {
        cardsDao.deleteUnauthedCard(UnauthedCardEntity.from(card))
    }
}